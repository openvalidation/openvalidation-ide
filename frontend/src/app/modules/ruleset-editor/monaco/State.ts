export class State implements monaco.languages.IState {
  public lineIndex: number;

  constructor(lineIndex: number) {
    this.lineIndex = lineIndex;
  }

  public clone(): State {
    return new State(this.lineIndex);
  }

  public equals(other: monaco.languages.IState): boolean {
    if (other === this) {
      return true;
    }
    if (!other || !(other instanceof State)) {
      return false;
    }
    return this.lineIndex === (other as State).lineIndex;
  }
}
