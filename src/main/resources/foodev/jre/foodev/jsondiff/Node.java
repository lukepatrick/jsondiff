package foodev.jsondiff;

abstract class Node implements Cloneable {

	// keep the original hash code since we'll be unsetting the parent leaf
	int hashCode, parentHashCode;

	Node parent;
	Leaf leaf;

	Node(Node parent) {
		this.parent = parent;
	}

	protected abstract Node clone() ;

	abstract int doHash(boolean indexed);

	@Override
	public int hashCode() {
		return doHash(false);
	}

	boolean isOrphan() {
		return parent == null;
	}

	void orphan() {
		parent = null;
		if (leaf != null) {
			for (Leaf c : leaf.newStructure) {
				c.parent.orphan();
			}
		}
	}
}