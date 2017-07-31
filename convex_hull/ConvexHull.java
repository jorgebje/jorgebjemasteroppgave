class ConvexHull{
	public static int[] start(int[] x, int[] y, int start, int stop){
		int n = stop-start;

		//find left and right
		int left_id = start;
		int right_id = start;
		for(int i = start+1; i < stop; i++){
			if(x[i] < x[left_id] || (x[i] == x[left_id] && y[i] < y[left_id])){
				left_id = i;
			}
			if(x[i] > x[right_id] || (x[i] == x[right_id] && y[i] > y[right_id])){
				right_id = i;
			}
		}

		// create id list
		int[] id = new int[n];

		//find max, min, and fill id
		int max = -1;
		int min = -1;
		int max_val = 0;
		int min_val = 0;
		int max_pos = 0;
		int min_pos = n-1;

		for(int i = 0; i < n; i++){
			int p_i = i+start;
			int dist = getDistFromLine(x[left_id],y[left_id],x[right_id],y[right_id],x[p_i],y[p_i]);
			if(dist > 0){
				if(dist > max_val){
					if(max != -1){
						id[max_pos++] = max;
					}
					max = p_i;
					max_val = dist;
				}
				else{
					//add to left side of id
					id[max_pos++] = p_i;
				}
			}
			else if(dist < 0){
				if(dist < min_val){
					if(min != -1){
						id[--min_pos] = min;
					}
					min = p_i;
					min_val = dist;
				}
				else{
					//add to right side of id
					id[--min_pos] = p_i;
				}
			}
			else{
				if(x[left_id] == x[p_i] && y[left_id] == y[p_i] && left_id != p_i){
					id[max_pos++] = p_i;
				}
				else if(x[right_id] == x[p_i] && y[right_id] == y[p_i] && right_id != p_i){
					id[--min_pos] = p_i;
				}
			}
		}

		int max_end;
		if(max == -1){
			max_pos = 0;
			//check for no max
			//if no max, find points on line and add them in correct order
			for(int i = 0; i < n; i++){
				int p_i = i+start;
				int dist = getDistFromLine(x[left_id],y[left_id],x[right_id],y[right_id],x[p_i],y[p_i]);
				if(dist == 0 && left_id != p_i && right_id != p_i && (x[right_id] != x[p_i] || y[right_id] != y[p_i])){
					id[max_pos++] = p_i;
				}
			}
			distSort(x,y,id, 0, max_pos, left_id);
			max_end = max_pos-1;
		}
		else{
			id[max_pos] = max;
			max_end = findConvexHullPart(x,y,id,
										0,max_pos,
										left_id,max,right_id);
		}

		int min_end;
		if(min == -1){
			min_pos = n-1;
			//check for no min
			//if no min, find points on line and add them in correct order
			for(int i = 0; i < n; i++){
				int p_i = i+start;
				int dist = getDistFromLine(x[left_id],y[left_id],x[right_id],y[right_id],x[p_i],y[p_i]);
				if(dist == 0 && left_id != p_i && right_id != p_i && (x[left_id] != x[p_i] || y[left_id] != y[p_i])){
					id[--min_pos] = p_i;
				}
			}
			distSort(x,y,id, min_pos, n-1, right_id);
			min_end = n-2;
		}
		else{
			id[n-1] = min;
			min_end = findConvexHullPart(x,y,id,
										min_pos,n-1,
										right_id,min,left_id);
		}

		//create a new array with only the results
		int[] ret_id = new int[1 + (max_end+1) + 1 + ((min_end+1) - min_pos)];
		int ret_pos = 0;
		ret_id[ret_pos++] = left_id;
		for(int i = 0; i <= max_end; i++){
			ret_id[ret_pos++] = id[i];
		}
		ret_id[ret_pos++] = right_id;
		for(int i = min_pos; i <= min_end; i++){
			ret_id[ret_pos++] = id[i];
		}
		return ret_id;
	}

	/**
	 * @param p1  coordinates for the first point
	 * @param p2  coordinates for the second point
	 *            together p1 and p2 makes a line
	 * @param p3  coordinates for the point to check
	 *
	 * @return    distance from line (p1,p2)
	**/
	public static int getDistFromLine(int x1, int y1, int x2, int y2, int x3, int y3){
		//find dist of point from line (x1,y1),(x2,y2)

		int a = y1 - y2; 
		int b = x2 - x1; 
		int c = y2 * x1 - y1 * x2;

		//in our case there is no need to calculate Math.sqrt(a*a+b*b)
		int dist_from_line = a*x3+b*y3+c;

		return dist_from_line;
	}
	private static void distSort(int[] x, int[] y, int[] id, int id_start, int id_stop, int origin_id){
		int[] dist = new int[id_stop - id_start];
		int dist_pos = 0;
		for(int i = id_start; i < id_stop; i++){
			int current_id = id[i];
			dist[dist_pos++] = (x[origin_id]-x[current_id])*(x[origin_id]-x[current_id]) + (y[origin_id]-y[current_id])*(y[origin_id]-y[current_id]);
		}
		// insertionSort(id,dist,id_start);
		mergeSort(dist,id,0,dist.length,id_start);
		return;
	}


	private static void insertionSort(int id[],int dist[], int from){
		for(int i = 1; i < dist.length; i++){
			for(int j = i; j > 0; j--){
				if(dist[j] < dist[j-1]){
					swap(dist,j-1,j);
					swap(id,from+j-1,from+j);
				}
			}
		}
	}

	private static void mergeSort(int[] dist, int[] id, int dist_from, int dist_to, int offset){
		if(dist_to - dist_from < 2) return;


		int mid = dist_from + (dist_to-dist_from)/2;
		mergeSort(dist,id,dist_from,mid,offset);
		mergeSort(dist,id,mid,dist_to,offset);

		int[] dist_work = new int[dist_to-dist_from];
		int[] id_work = new int[dist_to-dist_from];

		int a = dist_from;
		int b = mid;

		int i = 0;
		while(a < mid && b < dist_to){			
			if(dist[a] < dist[b]){
				dist_work[i] = dist[a];
				id_work[i++] = id[offset+a++];
			}
			else{
				dist_work[i] = dist[b];
				id_work[i++] = id[offset+b++];
			}
		}
		while(a < mid){
			dist_work[i] = dist[a];
			id_work[i++] = id[offset+a++];
		}
		while(b < dist_to){
			dist_work[i] = dist[b];
			id_work[i++] = id[offset+b++];
		}


		for(i = 0; i < dist_work.length; i++){
			dist[dist_from] = dist_work[i];
			id[offset + dist_from++] = id_work[i];
		}

	}

	static void swap(int[] id, int p1, int p2){
		int temp = id[p1];
		id[p1] = id[p2];
		id[p2] = temp;
	}

	//[[outside][data_to_look_through][outside]]
	/**
	 * @param  line, (left_id,right_id)
	 * @return pos of highest/or -1 if no one found
	 * id sorted if true:  [[outside][points over line][highest][points under line][outside]]
	**/

	private static int findMaxAndSort(
		int[] x, int y[],
		int[] id, int start, int stop,
		int left_id, int right_id
		){
		int max_val = 0;
		int max_pos = start;
		for(int i = start; i < stop; i++){
			int dist = getDistFromLine(x[left_id],y[left_id],x[right_id],y[right_id],x[id[i]],y[id[i]]);
			if(dist > 0){
				if(dist > max_val){
					if(max_val != 0){
						max_pos++;
					}
					swap(id,max_pos,i);
					max_val = dist;
				}
				else{
					//move max one up, put the newly found in max old spot
					swap(id,max_pos,max_pos+1);
					if(i != max_pos+1)
						swap(id,max_pos,i);
					max_pos++;
				}
			}
			else if(dist == 0){
				//keep points on the left point
				if(x[left_id] == x[id[i]] && y[left_id] == y[id[i]]){
					if(max_val == 0){
						swap(id,max_pos,i);
						max_pos++;
					}
					else{
						swap(id,max_pos,max_pos+1);
						swap(id,max_pos,i);
						max_pos++;
					}
				}
			}
		}
		if(max_val == 0)
			return -1;
		return max_pos;
	}


	//[[outside][data_to_look_through][outside]]
	/**
	 * @param  line, (left_id,right_id)
	 * @return last element on line
	 * id sorted:  [[outside][points on line][points under line][outside]]
	**/
	static int findPointsOnLine(int[] x, int[] y, int[] id, int id_start, int id_stop, int left_id, int right_id){
		int max_pos = id_start;
		for(int i = id_start; i < id_stop; i++){
			int dist = getDistFromLine(x[left_id],y[left_id],x[right_id],y[right_id],x[id[i]],y[id[i]]);
			if(dist == 0){
				//swap into the line
				swap(id,max_pos,i);
				max_pos++;
			}
		}
		if(max_pos != id_start){
			//found points on line, sort them
			distSort(x,y,id,id_start,max_pos,left_id);
		}
		//return the last element
		return max_pos-1;
	}

	/**
	 * @param x, x-coordinates
	 * @param y, y-coordinates
	 * @param id, list
	 * @param start_pos, start pos in id
	 * @param stop_pos, stop pos in id
	 * @param left_id, top_id, right_id. All part of two lines (left_id,top_id) and (top_id,right_id)
	 *
	 * id at start:  [[outside],[start_pos,...,stop_pos-1],top_id,[outside]]
	 * id at return: [[outside],[left_start,...,left_stop],top_id,[right_start,...,right_stop],[unused],[outside]]
	 *
	 * @return position of last element, right_stop
	**/

	static int findConvexHullPart(
		int[] x, int y[],
		int[] id, int start_pos, int stop_pos,
		int left_id, int top_id, int right_id
		){

		//id: [outside][to be sorted][top][outside]

		//find left side
		int max_pos = findMaxAndSort(x,y,id,start_pos,stop_pos,left_id,top_id);
		int left_stop;
		if(max_pos == -1){
			left_stop = findPointsOnLine(x,y,id,start_pos,stop_pos,left_id,top_id);
		}
		else{
			//recursive call
			int max_id = id[max_pos];
			left_stop = findConvexHullPart(x,y,id,start_pos,max_pos,left_id,max_id,top_id);
		}

		int top_pos = left_stop+1;
		//insert top into the middle
		swap(id,top_pos,stop_pos);

		//right starts after top
		int right_start = top_pos+1;

		//id: [outside][leftside][top][to be sorted][outside]

		//now find the right side, line (top,right)

		//find right side
		max_pos = findMaxAndSort(x,y,id,right_start,stop_pos+1,top_id,right_id);
		int right_end;
		if(max_pos == -1){
			//no more points outside line
			right_end = findPointsOnLine(x,y,id,right_start,stop_pos+1,top_id,right_id);
		}
		else{
			int max_id = id[max_pos];
			right_end = findConvexHullPart(x,y,id,right_start,max_pos,top_id,max_id,right_id);
		}

		//id: [outside][leftside][top][rightside][unused][outside]

		return right_end;
	}
}