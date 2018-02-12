from py4j.java_gateway import JavaGateway
from py4j.java_gateway import GatewayParameters
import cStructure

'''
Hi! You can use this code as a template to create your own bot.  Also if you don't mind writing a blurb
about your bot's strategy you can put it as a comment here. I'd appreciate it, especially if I can help
debug any runtime issues that occur with your bot.
'''

# Optional Information. Fill out only if you wish.

# Your real name: Tyler Arehart
# Contact Email: tarehart@gmail.com
# Can this bot's code be shared publicly (Default: No): No
# Can non-tournment gameplay of this bot be displayed publicly (Default: No): Yes

# This is the name that will be displayed on screen in the real time display!
BOT_NAME = "ReliefBot"

class agent:

	def __init__(self, team):
		self.team = team # use self.team to determine what team you are. I will set to "blue" or "orange"

		self.myPort = 25368
		try:
			with open("port.txt", "r") as portFile:
				self.myPort = int(portFile.readline())
		except ValueError:
			print("Failed to parse port file! Will proceed with hard-coded port number.")
		except:
			pass

		try:
			self.init_py4j_stuff()
		except:
			print("Exception when trying to connect to java! Make sure the java program is running!")
			pass

	def init_py4j_stuff(self):
		print("Connecting to Java Gateway on port " + str(self.myPort))
		self.gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_convert=True, port=self.myPort))
		self.javaAgent = self.gateway.entry_point.getAgent()
		print("Connection to Java successful!")

	def get_output_vector(self, sharedValue):
		try:
			input_json = cStructure.gameTickPacketToJson(sharedValue.GameTickPacket)
			# Call the java process to get the output
			listOutput = self.javaAgent.getOutputVector(input_json, self.team)
			# Convert to a regular python list
			return list(listOutput)
		except Exception as e:
			print("Exception when calling java: " + str(e))
			print("Will recreate gateway...")
			self.gateway.shutdown_callback_server()
			try:
				self.init_py4j_stuff()
			except:
				print("Reinitialization failed")
				pass

			return [16383, 16383, 0, 0, 0, 0, 0] # No motion
