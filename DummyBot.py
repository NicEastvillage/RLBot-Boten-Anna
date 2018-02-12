BOT_NAME = "StatueBot"

class agent:

	def __init__(self, team):
		self.team = team # use self.team to determine what team you are. I will set to "blue" or "orange"

	def get_output_vector(self, input):
		return [16383, 16383, 0, 0, 0, 0, 0]
	