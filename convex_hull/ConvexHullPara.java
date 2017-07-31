import java.util.concurrent.CyclicBarrier;

class ConvexHullPara{
	int t; // number of threads
	Worker[] workers;
	Thread[] threads;

	boolean open = false;

	CyclicBarrier CB; //used at start stop

	int[][] id;
	int[] x;
	int[] y;

	ConvexHullPara(){}

	ConvexHullPara(int t){
		open(t);
	}

	public void open(int t){
		if(open){
			close();
		}
		open = true;
		this.t = t;
		CB = new CyclicBarrier(t+1);
		workers = new Worker[t];
		threads = new Thread[t];
		for(int i = 0; i < t; i++){
			workers[i] = new Worker(i);
			(threads[i] = new Thread(workers[i])).start();
		}
		id = new int[t][];
	}

	public void close(){
		open = false;
		try{
			CB.await();
		}catch(Exception e){}
		for(int i = 0; i < t; i++){
			try{
				threads[i].join();
			}catch(Exception e){}
		}
	}

	public int[] start(int[] x, int[] y){
		//split points between workers
		this.x = x;
		this.y = y;
		for(int i = 0; i < t; i++){
			int start = (int)(i*(x.length/(double)t));
			int stop = (int)((i+1)*(x.length/(double)t));
			int size = stop-start;

			workers[i].offset = start;
			workers[i].size = size;
		}
		//start workers
		try{
			CB.await();
		}catch(Exception e){}

		//wait for workers
		try{
			CB.await();
		}catch(Exception e){}

		//merge convex hulls

		int[] id = mergeConvexHulls(this.id,x,y);

		return id;
	}

	/**
	 *  Recursive method to merge n hulls
	 *  Parts of the convex hull
	 *  A convex hull from points
	**/
	static int[] mergeConvexHulls(int[][] id, int[] x, int[] y){

		/* merge by new call to ConvexHull.start() */
		int xy_length = 0;
		for(int i = 0; i < id.length; i++){
			xy_length += id[i].length;
		}

		int[][] xy = new int[3][xy_length];
		int xy_pos = 0;
		for(int i = 0; i < id.length; i++){
			for(int j = 0; j < id[i].length; j++){
				xy[0][xy_pos] = x[id[i][j]];
				xy[1][xy_pos] = y[id[i][j]];
				xy[2][xy_pos++] = id[i][j];
			}
		}

		int[] id2 = ConvexHull.start(xy[0],xy[1],0,xy_length);

		int[] id3 = new int[id2.length];

		for(int i = 0; i < id2.length; i++){
			id3[i] = xy[2][id2[i]];
		}

		return id3;
		/* end */

		/* merge into one ball */
		// int[] ch = id[0];
		// for(int i = 1; i < id.length; i++){
		// 	int[][] to_merge = new int[][]{ch,id[i]};
		// 	ch = mergeNHulls(to_merge,x,y);
		// }
		// return ch;
		/* END merge into one ball */

		/* merge binary */
		// int[][] new_id;
		// while(id.length > 1){
		// 	if(id.length % 2 == 0){
		// 		new_id = new int[id.length/2][];
		// 	}
		// 	else{
		// 		new_id = new int[id.length/2+1][];
		// 	}
		// 	for(int i = 0; i < new_id.length; i++){
		// 		if(i*2+1 == id.length){
		// 			new_id[i] = id[i*2];
		// 		}
		// 		else{
		// 			int[][] to_merge = new int[][]{id[i*2],id[i*2+1]};
		// 			new_id[i] = mergeNHulls(to_merge,x,y);
		// 		}
		// 	}
		// 	id = new_id;
		// }
		// return id[0];
		/* END merge binary */

		/* merge all at once */
		// return mergeNHulls(id,x,y);
		/* END merge all at once

		/* dont do anyting */
		// return id[0];
	}

	public static int[] mergeNHulls(int[][] id, int[] x, int[] y){
		//select leftmost, lowermost
		int start = id[0][0];
		int start_arr = 0;
		for(int i = 1; i < id.length; i++){
			if(x[id[i][0]] < x[start] || (x[id[i][0]] == x[start] && y[id[i][0]] < y[start])){
				start = id[i][0];
				start_arr = i;
			}
		}

		int[] current_pos = new int[id.length];
		int[] next_pos = new int[id.length];
		int[] arr_length = new int[id.length];
		for(int i = 0; i < id.length; i++){
			current_pos[i] = 0;
			next_pos[i] = 1;
			arr_length[i] = id[i].length;
		}
		current_pos[start_arr] = next_pos[start_arr];
		next_pos[start_arr] = (next_pos[start_arr]+1)%arr_length[start_arr];

		int back_size = 0;
		for(int i = 0; i < id.length; i++){
			back_size += id[i].length;
		}
		int[] back = new int[back_size];
		back[0] = start;
		int back_pos = 1;

		int current = start;
		int current_arr = start_arr;
		//loop until complete
		while(true){
			int selected = -1;
			int selected_arr = -1;
			if(selected == -1){
				//validate all other paths
				for(int i = 0; i < id.length; i++){
					if(i != current_arr){
						int path_type;
						do{
							//validate a path

							int vcp = current_pos[i];
							int vnp = next_pos[i];
							path_type = ConvexHull.getDistFromLine(x[current], y[current], x[id[i][vcp]], y[id[i][vcp]], x[id[i][vnp]], y[id[i][vnp]]);
							if(path_type > 0){
								current_pos[i] = next_pos[i];
								next_pos[i] = (next_pos[i]+1)%arr_length[i];
							}
						}while(path_type > 0);
					}

				}
				//select correct path, if multiple select closest
				selected = id[0][current_pos[0]];
				selected_arr = 0;
				for(int i = 1; i < id.length; i++){
					int to_check = id[i][current_pos[i]];
					int dist = ConvexHull.getDistFromLine(x[current], y[current], x[selected], y[selected], x[to_check], y[to_check]);
					if(dist > 0){
						//this is a better choice
						selected = to_check;
						selected_arr = i;
					}
					else if(dist == 0){
						//both on line, select closest
						int dist_selected = (x[current] - x[selected])*(x[current] - x[selected]) + (y[current] - y[selected])*(y[current] - y[selected]);
						int dist_to_check = (x[current] - x[to_check])*(x[current] - x[to_check]) + (y[current] - y[to_check])*(y[current] - y[to_check]);
						if(dist_to_check < dist_selected){
							selected = to_check;
							selected_arr = i;
						}
					}
				}
			}
			if(selected == start){
				//if selected == start, break, we have gone full circle
				break;
			}
			else{
				//add selected to back
				back[back_pos] = selected;
				back_pos++;
				//shift up
				current_pos[selected_arr] = next_pos[selected_arr];
				next_pos[selected_arr] = (next_pos[selected_arr]+1)%arr_length[selected_arr];
				current = selected;
				current_arr = selected_arr;				
			}
		}//end loop

		//trim
		int[] trimmed_back = new int[back_pos];
		for(int i = 0; i < back_pos; i++){
			trimmed_back[i] = back[i];
		}
		return trimmed_back;

	}





	class Worker implements Runnable{
		int worker_id;
		int offset;
		int size;

		Worker(int worker_id){
			this.worker_id = worker_id;
		}

		public void run(){
			while(true){
				//wait for task
				try{
					CB.await();
				}catch(Exception e){}
				if(!open){
					break;
				}

				//calculate convex hull part
				id[worker_id] = ConvexHull.start(x,y,offset,offset+size);
				try{
					CB.await();
				}catch(Exception e){}
			}
		}
	}
}