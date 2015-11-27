package geon.game.topview;

import java.util.LinkedList;
import java.util.Random;

/**
 * <pre>
 * geon.game.topview
 * 	|_MazeMaker
 * 
 * 개요 : 
 * 작성일 : Nov 25, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */

public class MazeMaker {
	public final int[][] DIR = {
		{-1, 0}, {0, 1}, {1, 0}, {0, -1}
	};
	public int MAZE_HEIGHT = 16, MAZE_WIDTH = 16;
	public class Cell {
		public int[] wall = new int[4]; {
			wall[0] = wall[1] = wall[2] = wall[3] = 1;
		}
		@Override
		public String toString() {
			return String.format("%d %d %d %d ", wall[0], wall[1], wall[2], wall[3]);
		}
	}
	private final Cell[][] maze = new Cell[MAZE_HEIGHT + 2][];
	private final boolean[][] chk = new boolean[MAZE_HEIGHT + 2][]; {
		for (int i = 0; i < MAZE_HEIGHT + 2; i++){
			chk[i] = new boolean[MAZE_WIDTH + 2];
			maze[i] = new Cell[MAZE_WIDTH + 2];
			for (int j = 0; j < MAZE_WIDTH + 2; j++) {
				chk[i][j] = false;
				maze[i][j] = new Cell();
				if(i == MAZE_HEIGHT / 2 && j == 1) {
					maze[i][j].wall[3] = 0;
				}
				if(i == MAZE_HEIGHT / 2 && j == MAZE_WIDTH) {
					maze[i][j].wall[1] = 0;
				}
				if(i == 0 || i == MAZE_HEIGHT + 1 || j == 0 || j == MAZE_WIDTH + 1) {
					chk[i][j] = true;
				}
			}
		}
	}
	private final Random rand = new Random();
	class CellPos {
		int h, w;

		public CellPos (int h, int w) {
			this.h = h;
			this.w = w;
		}
	}
	LinkedList<CellPos> list = new LinkedList<>();
	public Cell[][] makeMaze() {
		list.add(new CellPos(1, 1));
		chk[1][1] = true;
		for(;;) {
			int i = rand.nextInt(list.size());
			CellPos pos = list.get(i);
			int[] branchable = new int[4];
			int branchableCnt = 0;
			for(int j = 0; j < 4; j++) {
				if(!chk[pos.h + DIR[j][0]][pos.w + DIR[j][1]]) {
					branchable[branchableCnt++] = j;
				}
			}
			if(branchableCnt > 0) {
				int d = branchable[rand.nextInt(branchableCnt)];
				int nh = pos.h + DIR[d][0], nw = pos.w + DIR[d][1];
				chk[nh][nw] = true;
				maze[pos.h][pos.w].wall[d] = maze[nh][nw].wall[(d + 2) % 4] = 0;
				list.add(new CellPos(nh, nw));
			} else {
				if(list.size() > 1)
					list.remove(i);
				else break;
			}
		}
		return maze;
	}
}