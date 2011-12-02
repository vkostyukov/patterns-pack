from threading import Thread 

class Elevator:
    def __init__(self):
        self.currentFloor = 0
        self.state = ElevatorIdle()
        self.callsQueue = []
    
    def call(self, floor):
        self.state.call(self, floor)
    
    def go(self, floor):
        self.currentFloor = floor
        print "Elevator went to " + str(floor) + " floor"
        if (len(self.callsQueue) > 0):
            nextFloor = self.callsQueue[-1] 
            self.callsQueue = self.callsQueue[:-1]
            self.go(nextFloor)
        else:
            self.setState(ElevatorIdle())

    def setState(self, state):
        self.state = state

class ElevatorState:
    def call(self, elevator, floor):
        raise NotImplementedError()
    
class ElevatorIdle(ElevatorState):
    def call(self, elevator, floor):
        elevator.setState(ElevatorBusy())
        elevator.go(floor)
    
class ElevatorBusy(ElevatorState):
    def call(self, elevator, floor):
        elevator.callsQueue.append(floor)
        
class Resident(Thread):
    
    def __init__(self, elevator, floor):
        Thread.__init__(self)
        self.floor = floor
        self.elevator = elevator
    
    def callElevator(self):
        print "Resident have been call elevator from " + str(self.floor) + " floor"
        self.elevator.call(self.floor)
    
    def run(self):
        self.callElevator()

def main():        

	elevator = Elevator()
	residents = Resident(elevator, 1), Resident(elevator, 2), Resident(elevator, 3), Resident(elevator, 4), Resident(elevator, 5)

	for resident in residents:
		resident.start()


if __name__ == "__main__": main()   

    