#disemvoweler - daily programmer

def disemvoweler(string):
	reduced = [c for c in string if c not in "aeiou"]
	return "".join(reduced).replace(" ", "")
print(disemvoweler("two drums and a cymbal fall off a cliff"))

def hello(a):
	return "hello "+ str(a)

print(list(map(hello, "aeiou")))

print([hello(x) for x in "aeiou"])