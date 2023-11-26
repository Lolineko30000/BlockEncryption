import os

n = [x for x in os.listdir('./') if '.class' in x]
nn = [x for x in os.listdir('./Encrypt') if '.class' in x]

a = n + list(map(lambda x: f'./Encrypt/{x}' ,nn))

list(map(os.remove, a))