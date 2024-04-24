class Grammar():
    def __init__(self):
        self.P = {
            'S': ['bA', 'AC'],
            'A': ['bS', 'BC', 'AbAa', 'bAa'],
            'B': ['a', 'bSa'],
            'C': ['eps'],
            'D': ['AB']
        }
        self.V_N = ['S', 'A', 'B', 'C', 'D']
        self.V_T = ['a', 'b']

    def Remove_Epsilon(self):
        nt_epsilon = []
        for key, value in self.P.items():
            s = key
            productions = value
            for p in productions:
                if p == 'eps':
                    nt_epsilon.append(s)

        P1 = self.P.copy()
        for key, value in self.P.items():
            for ep in nt_epsilon:
                for v in value:
                    prod_copy = v
                    if ep in prod_copy:
                        for c in prod_copy:
                            if c == ep:
                                value.append(prod_copy.replace(c, ''))

        for key, value in self.P.items():
            if key in nt_epsilon and len(value) < 2:
                del P1[key]
            else:
                for v in value:
                    if v == 'eps':
                        P1[key].remove(v)

        print(f"1. After removing epsilon productions:\n{P1}")
        self.P = P1.copy()
        return P1

    def Eliminate_Unit_Prod(self):
        P2 = self.P.copy()
        for key, value in self.P.items():
            for v in value:
                if len(v) == 1 and v in self.V_N:
                    P2[key].remove(v)
                    for p in self.P[v]:
                        P2[key].append(p)
        print(f"2. After removing unit productions:\n{P2}")
        self.P = P2.copy()
        return P2

    def Eliminate_Inaccesible_Symbols(self):
        P3 = self.P.copy()
        accesible_symbols = self.V_N
        for key, value in self.P.items():
            for v in value:
                for s in v:
                    if s in accesible_symbols:
                        accesible_symbols.remove(s)

        for el in accesible_symbols:
            del P3[el]
        print(f"3. After removing inaccessible symbols:\n{P3}")
        self.P = P3.copy()
        return P3

    def Remove_Nonproductive(self):
        P4 = self.P.copy()
        for key, value in self.P.items():
            count = 0
            for v in value:
                if len(v) == 1 and v in self.V_T:
                    count += 1
            if count == 0 and key not in self.V_T:
                del P4[key]
                for k, v in self.P.items():
                    for e in v:
                        if k == key:
                            break
                        else:
                            if key in e:
                                P4[k].remove(e)

        for key, value in self.P.items():
            for v in value:
                for c in v:
                    if c.isupper() and c not in P4.keys() and c != key:
                        P4[key].remove(v)
                        break

        print(f"4. After removing non-productive symbols:\n{P4}")
        self.P = P4.copy()
        return P4

    def Chomsky_Normal_Form(self):
        P5 = self.P.copy()
        temp = {}
        vocabulary = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                      'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
        free_symbols = [v for v in vocabulary if v not in self.P.keys()]
        for key, value in self.P.items():
            for v in value:
                if (len(v) == 1 and v in self.V_T) or (len(v) == 2 and v.isupper()):
                    continue
                else:
                    left = v[:len(v) // 2]
                    right = v[len(v) // 2:]
                    if left in temp.values():
                        temp_key1 = ''.join([i for i in temp.keys() if temp[i] == left])
                    else:
                        temp_key1 = free_symbols.pop(0)
                        temp[temp_key1] = left
                    if right in temp.values():
                        temp_key2 = ''.join([i for i in temp.keys() if temp[i] == right])
                    else:
                        temp_key2 = free_symbols.pop(0)
                        temp[temp_key2] = right
                    P5[key] = [temp_key1 + temp_key2 if item == v else item for item in P5[key]]

        for key, value in temp.items():
            P5[key] = [value]

        print(f"5. Final CNF:\n{P5}")
        return P5

    def Return_Productions(self):
        print(f"Initial Grammar:\n{self.P}")
        P1 = self.Remove_Epsilon()
        P2 = self.Eliminate_Unit_Prod()
        P3 = self.Eliminate_Inaccesible_Symbols()
        P4 = self.Remove_Nonproductive()
        P5 = self.Chomsky_Normal_Form()
        return P1, P2, P3, P4, P5


if __name__ == "__main__":
    g = Grammar()
    P1, P2, P3, P4, P5 = g.Return_Productions()
