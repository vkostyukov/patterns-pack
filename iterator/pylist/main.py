class List:
    def add(self, item):
        raise NotImplementedError();    
    def remove(self, item):
        raise NotImplementedError();    
    def iterator(self):
        raise NotImplementedError();
    def size(self):
        raise NotImplementedError();
    def get(self, index):
        raise NotImplementedError();
    def set(self, index, item):
        raise NotImplementedError();
    
    def __getitem__(self, index):
        return self.get(index)
    def __len__(self):
        return self.size()
    def __setitem__(self, index, item):
        self.set(index, item)
    def __iter__(self):
        return self.iterator()

class Iterator:
    def next(self):
        raise NotImplementedError();    
    def hasNext(self):
        raise NotImplementedError();
    def remove(self):
        raise NotImplementedError();
    
    def __iter__(self):
        return self

class ArrayList(List):
    def __init__(self):
        self.data = []
        
    def add(self, item):
        self.data.append(item)
    
    def remove(self, item):
        raise NotImplementedError()
        
    def iterator(self):
        return ArrayListIterator(self)
    
    def size(self):
        return len(self.data)
    
    def get(self, index):
        return self.data[index]
    
    def set(self, index, item):
        self.data[index] = item
    
class LinkedList(List):
    class Entry:
        def __init__(self, data):
            self.data = data
            self.next = None
            
    def __init__(self):
        self.head = None
    
    def add(self, item):
        if self.head == None:
            self.head = LinkedList.Entry(item)
        else:
            cursor = self.head
            while  cursor.next != None:
                cursor = cursor.next
            cursor.next = LinkedList.Entry(item)
             
    def remove(self, item):
        raise NotImplementedError()        
    
    def iterator(self):
        return LinkedListIterator(self)    

class ArrayListIterator(Iterator):
    def __init__(self, list):
        self.list = list
        self.index = 0
    
    def next(self):
        if self.hasNext():
            self.index = self.index + 1
            return self.list.get(self.index - 1)
        else:
            raise StopIteration()        
    
    def hasNext(self):
        return self.index < self.list.size()
    
    def remove(self):
        raise NotImplementedError()

class LinkedListIterator(Iterator):
    
    def __init__(self, list):
        self.list = list
        self.current = list.head
    
    def next(self):
        if self.hasNext():
            item = self.current.data
            self.current = self.current.next
            return item
        else:
            raise StopIteration()
    
    def hasNext(self):
        return self.current.next != None
    
    def remove(self):
        raise NotImplementedError() 

def main():

	aList = ArrayList()
	aList.add(10)
	aList.add(20)
	aList.add(30)
	aList.add(40)
	aList.add(50)

	for i in aList:
	    print i
    
	lList = LinkedList()
	lList.add(10)
	lList.add(20)
	lList.add(30)
	lList.add(40)
	lList.add(50)

	it = lList.iterator()
	while it.hasNext():
	    print it.next()

if __name__ == "__main__": main()
