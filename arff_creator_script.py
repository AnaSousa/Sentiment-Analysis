import re

dictionary = []

def generate_myarff():
	r = open("words.txt", "r")
	w = open("myarff.arff", "w")

	w.write("@RELATION feeling analysis\n\n")

	for l in r:
		if l.strip() != "":
			w.write("@ATTRIBUTE " + l.split(",")[0] + " NUMERIC\n")

	w.write("@ATTRIBUTE @@class@@ {neg,pos}")

def open_dictionary():
	r = open("words.txt", "r")

	for l in r:
		if l.strip() != "":
			dictionary.append(l.split(",")[0].strip())


def populate_myarff_pos():
	r = open("rt-polarity.pos", "r")
	w = open("posArff.txt", "a")

	for l in r:
		words = l.split(" ")
		#print str(words)
		indexes = [0] * len(dictionary)
		for idx, val in enumerate(dictionary):
			if(val in words):
				indexes[idx] = 1
				#print l
				#print "->" + val

		w.write("{")
		for i, val in enumerate(indexes):
			if(val != 0):
				w.write(str(i) + " 1, ")

		w.write(str(len(dictionary)) + " pos}\n")


def populate_myarff_neg():
	r = open("rt-polarity.neg", "r")
	w = open("negArff.txt", "w")

	for l in r:
		words = l.split(" ")
		#print str(words)
		indexes = [0] * len(dictionary)
		for idx, val in enumerate(dictionary):
			if(val in words):
				indexes[idx] = 1
				#print l
				#print "->" + val

		w.write("{")
		for i, val in enumerate(indexes):
			if(val != 0):
				w.write(str(i) + " 1, ")

		w.write(str(len(dictionary)) + " neg}\n")




	
	

open_dictionary()
#populate_myarff_pos()
populate_myarff_neg()

