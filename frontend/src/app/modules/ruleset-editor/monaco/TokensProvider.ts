import { Range } from 'monaco-languageclient';
import { State } from './State';

export function createTokenizationSupport(
  params: {
    range: Range;
    pattern: string;
  }[]
): monaco.languages.TokensProvider {
  const hashMap = generateTokensHashMap(params);
  return {
    getInitialState: () => new State(0),
    tokenize: (line: string, state: State) =>
      tokenize(state as State, line, hashMap)
  };
}

function tokenize(
  state: State,
  text: string,
  params: Map<number, monaco.languages.IToken[]>
): monaco.languages.ILineTokens {
  const ret = {
    tokens: [] as monaco.languages.IToken[],
    endState: new State(state.lineIndex + 1)
  };

  const something = params.get(state.lineIndex);
  if (!!something) {
    ret.tokens.push(...something);
  }

  return ret;
}

function generateTokensHashMap(
  params: { range: Range; pattern: string }[]
): Map<number, monaco.languages.IToken[]> {
  const returnMap = new Map<number, monaco.languages.IToken[]>();

  for (const param of params) {
    if (!param.range) { continue; }

    const startLine = param.range.start.line;

    // 1. just add the start
    if (!returnMap.get(startLine)) {
      const initialValue = [];
      if (param.range.start.character > 0) {
        initialValue.push({
          startIndex: 0,
          scopes: 'empty'
        });
      }

      returnMap.set(startLine, initialValue);
    }

    const currentValue: monaco.languages.IToken[] | undefined = returnMap.get(
      startLine
    );
    if (!currentValue) { continue; }

    currentValue.push({
      startIndex: param.range.start.character,
      scopes: param.pattern
    });

    const multipleLines: boolean =
      param.range.end.line - param.range.start.line >= 1;
    if (!multipleLines) {
      currentValue.push({
        startIndex: param.range.end.character,
        scopes: 'empty'
      });
    } else {
      for (
        let index = param.range.start.line + 1;
        index <= param.range.end.line;
        index++
      ) {
        let tmp = returnMap.get(index);
        if (!tmp) { tmp = []; }

        tmp.push({
          startIndex: 0,
          scopes: param.pattern
        });

        if (index === param.range.end.line) {
          tmp.push({
            startIndex: param.range.end.character,
            scopes: 'empty'
          });
        }

        returnMap.set(index, tmp);
      }
    }

    returnMap.set(startLine, currentValue);
  }

  for (const [key, value] of returnMap) {
    const sortedValue = value.sort(
      (n1: monaco.languages.IToken, n2: monaco.languages.IToken) =>
        n1.startIndex - n2.startIndex
    );
    returnMap.set(key, sortedValue);
  }

  return returnMap;
}
