import numpy as np
from collections import OrderedDict
from texttable import Texttable

class State:
	def __init__(self, name, propability):
		self.name = name
		self.propability = propability
	
	def prop(self, propability):
		self.temp = propability
	
	def finalize(self, factor):
		self.propability = self.temp/factor

class Expression:
	def __init__(self, name):
		self.name = name

class Score:
	def __init__(self, home, away, propability):
		self.home = home
		self.away = away
		self.propability = propability
		self.name = "{0}:{1}".format(self.home, self.away)
		self.temp = 0
	
	def prop(self, propability):
		self.temp += propability
	
	def finalize(self):
		self.propability = self.temp
		
ole = Expression("Ole")
tor = Expression("Toor!")
oh = Expression("Ohhh")

ka = State("KAE", 0.3333)
tw = State("TW", 0.3333)
tg = State("TG", 0.3333)

scores = OrderedDict({(0,0): Score(0,0,1)})

states = [ka, tw, tg]

transitionModel = {\
	(ka.name,ka.name): 0.6, (ka.name,tw.name): 0.2, (ka.name,tg.name): 0.2,\
	(tw.name,ka.name): 0.4, (tw.name,tw.name): 0.3, (tw.name,tg.name): 0.3,\
	(tg.name,ka.name): 0.3, (tg.name,tw.name): 0.2, (tg.name,tg.name): 0.2\
	}
sensorModel = {\
	(ole.name,ka.name): 0.8, (tor.name,ka.name): 0.05, (oh.name,ka.name): 0.15,\
	(ole.name,tg.name): 0.1, (tor.name,tg.name): 0.2, (oh.name,tg.name): 0.7,\
	(ole.name,tw.name): 0.1, (tor.name,tw.name): 0.8, (oh.name,tw.name): 0.1\
	}

def normalize():
		whole = np.sum([state.temp for state in states])
		for state in states:
			state.finalize(whole)
	
def recognize(sense):
		calculations = []
		for state in states:
			state.prop(sensorModel[(sense.name,state.name)] * np.sum([transitionModel[(state.name,otherState.name)] * otherState.propability for otherState in states]))
			calculations.append("P({0}|{1}) = P({1}|{0}) * (P({0}|{2}) * P({2}) + P({0}|{3}) * P({3}) + P({0}|{4}) * P({4})) = {5} ~= ".format(state.name, sense.name, ka.name, tw.name, tg.name, state.temp))
		normalize()
		
		print("")
		print("Calculations")
		for i,c in enumerate(calculations):
			print("{0}{1}".format(c, states[i].propability))
		
		old_scores = OrderedDict(scores)
		for score in old_scores:
			if (score[0]+1,score[1]) not in scores:
				scores[(score[0]+1,score[1])] = Score(score[0]+1, score[1], 0)
			if (score[0],score[1]+1) not in scores:
				scores[(score[0],score[1]+1)] = Score(score[0], score[1]+1, 0)
		
		for score in old_scores:			
			scores[(score[0]+1,score[1])].prop(scores[score].propability * tw.propability)
			scores[(score[0],score[1]+1)].prop(scores[score].propability * tg.propability)
			scores[score].prop(scores[score].propability * ka.propability)
		
		for score in scores:
			scores[score].finalize()
			scores[score].temp = 0		
		
		t = Texttable()
		t.add_row(["Score", "Wahrscheinlichkeit"])
		for score in scores:
			t.add_row([scores[score].name, scores[score].propability])
		
		t = Texttable()
		t.add_row(["Score", "Wahrscheinlichkeit"])
		for score in scores:
			t.add_row([scores[score].name, scores[score].propability])
			
		print("")
		print("Scores")
		print t.draw()
