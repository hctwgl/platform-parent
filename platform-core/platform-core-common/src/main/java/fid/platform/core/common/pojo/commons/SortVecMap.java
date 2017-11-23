package fid.platform.core.common.pojo.commons;

public class SortVecMap implements Comparable<SortVecMap>{

	private String label;
	
	private Double distance;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(SortVecMap o) {
		if(this.distance < o.distance){
			return 1;
		}else if(this.distance > o.distance){
			return -1;
		}
		return 0;
	}

	

}
