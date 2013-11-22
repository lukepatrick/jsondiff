package foodev.jsondiff;

class ArrNode extends Node {

	int index;

	ArrNode(Node parent, int index) {
		super(parent);
		this.index = index;
	}

	@Override
	protected Node clone() {
		ArrNode clone = new ArrNode(parent, index);
		clone.hashCode = hashCode;
		clone.parentHashCode = parentHashCode;
		clone.leaf = leaf;
		return clone;
	}

	@Override
	int doHash(boolean indexed) {

		// this must either be the first node in which case passing
		// false to lastArrNode must be correct, or it isn't
		// in which case passing false is also correct.
		int i = (parent == null) ? 0 : parent.doHash(indexed);

		i = i * 31 + 31 * ArrNode.class.hashCode();
		if (indexed) {
			int adjusted = index;
			i = i * 31 + adjusted;
		}
		return i;

	}

	@Override
	public String toString() {
		return "" + index;
	}
}