class Singletone():
        
    class SingletoneHelper():

        __instance = None
        
        def __call__(self, *args, **kw):
            print "calling SingletoneHelper__call__()"
            if self.__instance is None:
                self.__instance = Singletone()
            return self.__instance
          
    getInstance = SingletoneHelper();
        
    def __init__(self):
        print "calling Singletone.__init__()"


def main():
	s1 = Singletone.getInstance()
	s2 = Singletone.getInstance()
	s3 = Singletone.getInstance()
	
	print s1 == s2, s2 == s3

if __name__ == "__main__": main()