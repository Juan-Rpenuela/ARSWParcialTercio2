function Square({ value, onClick, disabled }) {
    return (
        <button 
            className={`square ${value ? value.toLowerCase() : ''}`}
            onClick={onClick}
            disabled={disabled}
        >
            {value}
        </button>
    );
}

function Board({ squares, onClick, disabled }) {
    const renderSquare = (i) => {
        return (
            <Square
                value={squares[i]}
                onClick={() => onClick(i)}
                disabled={disabled || squares[i] !== null}
            />
        );
    };

    return (
        <div>
            <div className="board-row">
                {renderSquare(0)}
                {renderSquare(1)}
                {renderSquare(2)}
            </div>
            <div className="board-row">
                {renderSquare(3)}
                {renderSquare(4)}
                {renderSquare(5)}
            </div>
            <div className="board-row">
                {renderSquare(6)}
                {renderSquare(7)}
                {renderSquare(8)}
            </div>
        </div>
    );
}


class Game extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            gameState: {
                squares: Array(9).fill(null),
                xIsNext: true,
                winner: null,
                stepNumber: 0,
                player1: null,
                player2: null,
                currentPlayer: null,
                status: 'WAITING'
            },
            playerId: 'player_' + Math.random().toString(36).substr(2, 9),
            connected: false,
            mySymbol: null,
            history: []
        };
        this.stompClient = null;
    }

    componentDidMount() {
        this.connectWebSocket();
    }

    componentWillUnmount() {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.disconnect();
        }
    }

    connectWebSocket = () => {
        const socket = new SockJS('/game');
        this.stompClient = Stomp.over(socket);

        this.stompClient.connect({}, 
            (frame) => {
                console.log('Conectado: ' + frame);
                this.setState({ connected: true });

                this.stompClient.subscribe('/topic/game', (message) => {
                    const gameState = JSON.parse(message.body);
                    console.log('Estado recibido:', gameState);
                    this.updateGameState(gameState);
                });

                this.joinGame();
            },
            (error) => {
                console.error('Error de conexión:', error);
                this.setState({ connected: false });
                setTimeout(this.connectWebSocket, 3000);
            }
        );
    };

    joinGame = () => {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.send('/app/join', {}, JSON.stringify({
                playerId: this.state.playerId,
                playerName: 'Jugador'
            }));
        }
    };

    updateGameState = (gameState) => {
        const { playerId } = this.state;
        let mySymbol = null;

        if (gameState.player1 === playerId) {
            mySymbol = 'X';
        } else if (gameState.player2 === playerId) {
            mySymbol = 'O';
        }

        this.setState({
            gameState: gameState,
            mySymbol: mySymbol
        });
    };

    handleClick = (i) => {
        const { gameState, playerId, mySymbol, connected } = this.state;

        if (!connected || !mySymbol) {
            alert('Esperando conexión...');
            return;
        }

        if (gameState.status !== 'PLAYING') {
            alert('El juego no ha comenzado o ya terminó');
            return;
        }

        if (gameState.currentPlayer !== playerId) {
            alert('No es tu turno');
            return;
        }

        if (gameState.squares[i]) {
            return;
        }

        this.stompClient.send('/app/move', {}, JSON.stringify({
            position: i,
            playerId: playerId,
            symbol: mySymbol
        }));

        const newHistory = [...this.state.history, {
            move: i,
            player: mySymbol
        }];
        this.setState({ history: newHistory });
    };

    handleReset = () => {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.send('/app/reset', {}, JSON.stringify({}));
            this.setState({ history: [] });
        }
    };

    getStatus = () => {
        const { gameState, mySymbol, playerId } = this.state;

        if (gameState.status === 'WAITING') {
            return {
                message: 'Esperando jugadores...',
                className: 'waiting'
            };
        }

        if (gameState.winner) {
            if (gameState.winner === 'Draw') {
                return {
                    message: '¡Empate!',
                    className: 'finished'
                };
            }
            const isWinner = gameState.winner === mySymbol;
            return {
                message: isWinner ? '¡Ganaste!' : 'Perdiste',
                className: 'finished'
            };
        }

        const isMyTurn = gameState.currentPlayer === playerId;
        return {
            message: isMyTurn ? 'Tu turno (' + mySymbol + ')' : 'Turno del oponente',
            className: 'playing'
        };
    };

    render() {
        const { gameState, connected, mySymbol, history } = this.state;
        const status = this.getStatus();

        return (
            <div className="game">
                <h1 className="game-title"> Tic-Tac-Toe Multijugador</h1>

                <div className={`connection-status ${connected ? 'connected' : 'disconnected'}`}>
                    {connected ? 'Conectado' : 'Desconectado'}
                </div>

                {mySymbol && (
                    <div style={{ textAlign: 'center', marginBottom: '15px' }}>
                        <strong>Eres: {mySymbol}</strong>
                    </div>
                )}

                <div className={`game-status ${status.className}`}>
                    {status.message}
                </div>

                <div className="game-board">
                    <Board
                        squares={gameState.squares}
                        onClick={this.handleClick}
                        disabled={!connected || gameState.status !== 'PLAYING'}
                    />
                </div>

                <div className="game-controls">
                    <button 
                        className="reset-button"
                        onClick={this.handleReset}
                        disabled={!connected}
                    >
                        Nueva Partida
                    </button>
                </div>

                <div className="game-info">
                    {gameState.player1 && gameState.player2 && (
                        <div className="player-info">
                            <div className={`player ${gameState.xIsNext ? 'active' : 'inactive'}`}>
                                Jugador X
                            </div>
                            <div className={`player ${!gameState.xIsNext ? 'active' : 'inactive'}`}>
                                Jugador O
                            </div>
                        </div>
                    )}

                    {history.length > 0 && (
                        <div className="history">
                            <h3>Historial de Movimientos</h3>
                            <ul className="history-list">
                                {history.map((item, index) => (
                                    <li key={index}>
                                        Movimiento {index + 1}: {item.player} en posición {item.move}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>
            </div>
        );
    }
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<Game />);