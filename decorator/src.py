class AbstractBlock:
    def draw(self):
        raise NotImplementedError();

class TerminatorBlock(AbstractBlock):
    def draw(self):
        print "Terminator block drawing ... "

class ProcessBlock(AbstractBlock):
    def draw(self):
        print "Process block drawing ... "

class AbstractBlockDecorator(AbstractBlock):
    def __init__(self, decoratee):
        self._decoratee = decoratee
    
    def draw(self):
        self._decoratee.draw()


class LabelBlockDecorator(AbstractBlockDecorator):
    def __init__(self, decoratee, label):
        self._decoratee = decoratee
        self._label = label
    
    def draw(self):
        AbstractBlockDecorator.draw(self)
        self._drawLabel()
    
    def _drawLabel(self):
        print " ... drawing label " + self._label

class BorderBlockDecorator(AbstractBlockDecorator):
    def __init__(self, decoratee, borderWidth):
        self._decoratee = decoratee
        self._borderWidth = borderWidth
    
    def draw(self):
        AbstractBlockDecorator.draw(self)
        self._drawBorder()
    
    def _drawBorder(self):
        print " ... drawing border with width " + str(self._borderWidth)


def main():
	tBlock = TerminatorBlock()
	pBlock = ProcessBlock()

	labelDecorator = LabelBlockDecorator(tBlock, "Label222")

	borderDecorator1 = BorderBlockDecorator(labelDecorator, 22)

	borderDecorator2 = BorderBlockDecorator(pBlock, 22)

	labelDecorator.draw()
	borderDecorator1.draw()
	borderDecorator2.draw()

if __name__ == "__main__": main()
