import java.io.File;
import java.util.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.IntStream;

public final class SmithWaterman{
	public static final void main(String[] args){
	System.out.println("アラインメントを計算する文字列を二つ入力して下さい。");
	System.out.print("1つ目の文字列を入力:");
    Scanner scan = new Scanner(System.in);
    String tempstr1 = (scan.next());
    System.out.println("1つ目の文字列: "+ tempstr1);
    /* 配列の先頭に適当な記号を置いておく */
    String str1 = ("$" + tempstr1);
    System.out.print("2つ目の文字列を入力:");
    String tempstr2 = (scan.next());
    System.out.println("2つ目の文字列: "+ tempstr2);
    String str2 = ("&" + tempstr2);
    SWalgorithm(str1, str2);
	}

	public static final void SWalgorithm(String str1, String str2){
	/* 左上から走査し、スコアの最大値を取る位置を見つけ出す */
		List arraymentlist1 = new ArrayList();
		List arraymentlist2 = new ArrayList();
		String arrayi[] = str1.split("");
		String arrayj[] = str2.split("");
		int penalty = 1;
		List maxscoreofi = new ArrayList();
		List maxscoreofj = new ArrayList();
		int scoreij = 0;
		int scoremax = 0;
		int score[][] = new int[arrayi.length][arrayj.length];
		for(int i = 0; i < score.length; i++){
			score[i][0] = 0;
		}
		for(int j = 0; j < score[0].length; j++){
			score[0][j] = 0;
		}
		for(int j = 1; j < score[0].length; j++){
			for(int i = 1; i < score.length; i++){
				int upvalue = score[i-1][j] - penalty;
				int sinistralvalue = score[i][j-1] - penalty;
				/* スコア関数でスコアを求める */
				if(arrayi[i].equals(arrayj[j])){
					scoreij = 1;
				}else if((arrayi[i].equals("-")) || (arrayj[j].equals("-"))){
					scoreij = -1;
				}else{
					scoreij = -1;
				}
				int upleftvalue = score[i - 1][j - 1] + scoreij;
				/* 0,upbalue,sinistralvalue,score[i-1,j-1]+scoreijから
				最大値を選び、score[i][j]に代入 */
				int maxij = 0;
				if(upvalue >= 0) maxij = upvalue;
				if(sinistralvalue >= maxij) maxij = sinistralvalue;
				if(upleftvalue >= maxij) maxij = upleftvalue;
				/* これまでのsocreの中で最大値をとった場合にはmaxsocreofiと
				maxsocreofjを初期化した上でi,jを代入
				これまでの最大値と同じ値をとった場合はi,jを追加 */
				score[i][j] = maxij;
				if(score[i][j] > scoremax){
					scoremax = score[i][j];
					maxscoreofi = new ArrayList<>();
					maxscoreofj = new ArrayList<>();
					maxscoreofi.add(i);
					maxscoreofj.add(j);
				}else if(score[i][j] == scoremax){
					maxscoreofi.add(i);
					maxscoreofj.add(j);
				}
			}
		}
		/* 走査の、より後ろの[i,j]成分を基準にするため、リストを逆転 */
		Collections.reverse(maxscoreofi);
		Collections.reverse(maxscoreofj);
		/* 最大値をとる配列から順にその由来をたどり、局所アラインメントを取り出す */
		for(int x = 0;x < maxscoreofj.size(); x++){
			List arrayment1 = new ArrayList();
			List arrayment2 = new ArrayList();
			int gapcounti = x;
			int gapcountj = x;
			int criteriali = (int)maxscoreofi.get(gapcounti);
			int criterialj = (int)maxscoreofj.get(gapcountj);
			for(int y = 1;y > 0; y++){
				if((criteriali == 0) || (criterialj == 0)) break;
				int criteria = score[criteriali][criterialj];
				int sinistralvalue = score[criteriali - 1][criterialj] - penalty;
				int upvalue = score[criteriali][criterialj - 1] - penalty;
				if(arrayi[criteriali].equals(arrayj[criterialj])){
						scoreij = 1;
					}else if((arrayi[criteriali].equals("-")) || (arrayj[criterialj].equals("-"))){
						scoreij = -1;
					}else{
						scoreij = -1;
					}
				int upleftvalue = score[criteriali - 1][criterialj - 1] + scoreij;
				if(criteria == upleftvalue){
					arrayment1.add(arrayi[criteriali]);
					arrayment2.add(arrayj[criterialj]);
					criteriali--;
					criterialj--;
				}else if(criteria == 0){
					break;
				}else if(criteria == upvalue){
					arrayment1.add("-");
					arrayment2.add(arrayj[criterialj]);
					criterialj--;
				}else{
					arrayment1.add(arrayi[criteriali]);
					arrayment2.add("-");
					criteriali--;
				}
			}
			Collections.reverse(arrayment1);
			Collections.reverse(arrayment2);
			arraymentlist1.add(arrayment1);
			arraymentlist2.add(arrayment2);
		}
		/* 取り出した局所アラインメントのうち、最長のものを表示し、スコアを計算する */
		System.out.println("Smith-Watermanアルゴリズムを用いた局所アラインメントの検出結果");
		int longestarnum = 0;
		for(int i = 0;i < arraymentlist1.size();i++){
			List templist = (List)arraymentlist1.get(i);
			if(templist.size() > longestarnum)
				longestarnum = templist.size();
		}
		for(int j = 0;j < arraymentlist1.size();j++) {
			List templist1 = (List)arraymentlist1.get(j);
			List templist2 = (List)arraymentlist2.get(j);
			if(longestarnum  == templist1.size()){
				System.out.print(arraymentlist1.get(j));
				System.out.print("と");
				System.out.println(arraymentlist2.get(j));
				int scorevalue = 0;
				for(int k = 0;k < templist1.size();k++){
					if(templist1.get(k).equals(templist2.get(k))){
						scorevalue = scorevalue + 1;
					}else{
						scorevalue = scorevalue - 1;
					}
				}
				System.out.print("このアラインメントにおけるscoreの計算結果:");
				System.out.printf("%d%n",scorevalue);
			}
		}
		
	}
}