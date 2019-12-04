package alg.apriori;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * Copyright (C), 2017 ,途牛科技有限公司
 *
 * @author HornLive
 * @data 创建时间：2017年3月9日 下午4:27:35
 * @version V2.0
 * @since
 */
public class AprioriTest01 {
	private int minSup;
	private static List<Set<String>> dataSet;

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		AprioriTest01 apriori = new AprioriTest01();

		// 设置最小支持度
		apriori.setMinSup(2);
		// 构造数据集 dim_item_name_rank_in
		List<List<String>> data = alg.apriori.FileReader.getDatabase("data/data");
		dataSet = apriori.toSet(data);

		// 构造频繁1项集
		List<Set<String>> f1Set = apriori.findF1Item(dataSet);
		apriori.printSet(f1Set, 1);
		List<Set<String>> f2Set = apriori.findF2ItemWithDest(f1Set, "data/out-F2-support");
		System.out.println("带目的地的二项集计算结束！");
		apriori.printSet(f2Set, 2);

		// int i = 2;
		// do {
		// result = apriori.arioriGen(result,null);
		// apriori.printSet(result, i);
		// i++;
		// } while (result.size() != 0);
		long endTime = System.currentTimeMillis();
		System.out.println("共用时： " + (endTime - startTime) + "ms");
	}

	public void setMinSup(int minSup) {
		this.minSup = minSup;
	}

	/**
	 * 构造原始数据集，可以为之提供参数，也可以不提供 如果不提供参数，将按程序默认构造的数据集 如果提供参数为文件名，则使用文件中的数据集
	 */
	List<Set<String>> toSet(List<List<String>> dataset) {
		dataSet = new ArrayList<Set<String>>();

		for (List basket : dataset) {
			dataSet.add(new TreeSet<String>(basket));
		}
		return dataSet;
	}

	/**
	 * 找出候选1项集
	 * 
	 * @param data
	 * @return result
	 */
	List<Set<String>> findF1Item(List<Set<String>> data) {
		List<Set<String>> result = new ArrayList<Set<String>>();
		Map<String, Integer> dc = new HashMap<String, Integer>();
		for (Set<String> basket : data) {
			for (String item : basket) {
				if (dc.containsKey(item)) {
					dc.put(item, dc.get(item) + 1);
				} else {
					dc.put(item, 1);
				}
			}
		}
		for (Entry entry : dc.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}

		Set<String> itemKeys = dc.keySet();
		Set<String> tempKeys = new TreeSet<String>();
		for (String str : itemKeys) {
			tempKeys.add(str);
		}

		for (String item : tempKeys) {
			if (dc.get(item) >= minSup) {
				Set<String> f1Set = new TreeSet<String>();
				f1Set.add(item);
				result.add(f1Set);
			}
		}
		return result;
	}

	/**
	 * 生成2项集（包含目的地的二项集）
	 * 
	 * @param preSet
	 * @return
	 * @throws IOException
	 *
	 */
	List<Set<String>> findF2ItemWithDest(List<Set<String>> f1set, String out) throws IOException {

		List<Set<String>> result = new ArrayList<Set<String>>();
		int preSetSize = f1set.size();

		for (int i = 0; i < preSetSize - 1; i++) {
			String[] word = f1set.get(i).toArray(new String[0]);

			for (int j = i + 1; j < preSetSize; j++) {
				String[] word2 = f1set.get(j).toArray(new String[0]);

//				// 判断非目的地过滤掉(两个都不是目的地的过滤掉)
//				if (!DestJudgeUtil.isDestination(word[0]) && !DestJudgeUtil.isDestination(word2[0])) {
//					continue;
//				}

				// 二项集都可连，无需判断
				Set<String> set = new TreeSet<String>();
				set.addAll(f1set.get(i));
				set.addAll(f1set.get(j));

				// 判断K项集是否需要剪切掉，如果不需要被cut掉，则加入到k项集的列表中
				if (!isNeedCut(f1set, set)) {
					result.add(set);
				}
			}
		}
		return checkSupport(result, out);// 返回的都是频繁K项集
	}

	/**
	 * 利用arioriGen方法由k-1项集生成k项集
	 * 
	 * @param preSet
	 * @return
	 * @throws IOException
	 *
	 */
	List<Set<String>> arioriGen(List<Set<String>> preSet, String out) throws IOException {

		List<Set<String>> result = new ArrayList<Set<String>>();
		int preSetSize = preSet.size();

		for (int i = 0; i < preSetSize - 1; i++) {
			for (int j = i + 1; j < preSetSize; j++) {
				String[] strA1 = preSet.get(i).toArray(new String[0]);
				String[] strA2 = preSet.get(j).toArray(new String[0]);
				if (isCanLink(strA1, strA2)) {// 判断两个k-1项集是否符合连接成K项集的条件
					Set<String> set = new TreeSet<String>();
					for (String str : strA1) {
						set.add(str);// 将strA1加入set中连成前K-1项集
					}
					set.add((String) strA2[strA2.length - 1]);// 连接成K项集
					// 判断K项集是否需要剪切掉，如果不需要被cut掉，则加入到k项集的列表中
					if (!isNeedCut(preSet, set)) {
						result.add(set);
					}
				}
			}
		}
		return checkSupport(result, out);// 返回的都是频繁K项集
	}

	/**
	 * 把set中的项集与数量集比较并进行计算，求出支持度大于要求的项集
	 * 
	 * @param set
	 * @return
	 * @throws IOException
	 */
	List<Set<String>> checkSupport(List<Set<String>> setList, String supportfile) throws IOException {

		List<Set<String>> result = new ArrayList<Set<String>>();
		boolean flag = true;
		int[] counter = new int[setList.size()];
		for (int i = 0; i < setList.size(); i++) {

			for (Set<String> dSets : dataSet) {
				if (setList.get(i).size() > dSets.size()) {
					flag = true;
				} else {
					for (String str : setList.get(i)) {
						if (!dSets.contains(str)) {
							flag = false;
							break;
						}
					}
					if (flag) {
						counter[i] += 1;
					} else {
						flag = true;
					}
				}
			}
		}

		// 支持度打印输出
		if (supportfile == null) {
			for (int i = 0; i < setList.size(); i++) {
				if (counter[i] >= minSup) {
					result.add(setList.get(i));
				}
			}
		} else {
			BufferedWriter bw = new BufferedWriter(new FileWriter(supportfile));
			for (int i = 0; i < setList.size(); i++) {
				if (counter[i] >= minSup) {
					bw.write(setList.get(i).toString() + " = " + counter[i]);
					bw.newLine();
					result.add(setList.get(i));
				}
			}
			bw.flush();
			bw.close();
		}
		return result;
	}

	/**
	 * 判断两个项集能否执行连接操作
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	boolean isCanLink(String[] s1, String[] s2) {
		boolean flag = true;
		if (s1.length == s2.length) {
			for (int i = 0; i < s1.length - 1; i++) {
				if (!s1[i].equals(s2[i])) {
					flag = false;
					break;
				}
			}
			if (s1[s1.length - 1].equals(s2[s2.length - 1])) {
				flag = false;
			}
		} else {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断set是否需要被cut
	 *
	 * @param setList
	 * @param set
	 * @return
	 */
	boolean isNeedCut(List<Set<String>> setList, Set<String> set) {// setList指频繁K-1项集，set指候选K项集
		boolean flag = false;
		List<Set<String>> subSets = getSubset(set);// 获得K项集的所有k-1项集
		for (Set<String> subSet : subSets) {
			// 判断当前的k-1项集set是否在频繁k-1项集中出现，如果出现，则不需要cut
			// 若没有出现，则需要被cut
			if (!isContained(setList, subSet)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 功能:判断k项集的某k-1项集是否包含在频繁k-1项集列表中
	 *
	 * @param setList
	 * @param set
	 * @return
	 */
	boolean isContained(List<Set<String>> setList, Set<String> set) {
		boolean flag = false;
		int position = 0;
		for (Set<String> s : setList) {
			String[] sArr = s.toArray(new String[0]);
			String[] setArr = set.toArray(new String[0]);
			for (int i = 0; i < sArr.length; i++) {
				if (sArr[i].equals(setArr[i])) {
					// 如果对应位置的元素相同，则position为当前位置的值
					position = i;
				} else {
					break;
				}
			}
			// 如果position等于数组的长度，说明已经找到某个setList中的集合与
			// set集合相同了，退出循环，返回包含
			// 否则，把position置为0进入下一个比较
			if (position == sArr.length - 1) {
				flag = true;
				break;
			} else {
				flag = false;
				position = 0;
			}
		}
		return flag;
	}

	/**
	 * 获得k项集的所有k-1项子集
	 *
	 * @param set
	 * @return
	 */
	List<Set<String>> getSubset(Set<String> set) {

		List<Set<String>> result = new ArrayList<Set<String>>();
		String[] setArr = set.toArray(new String[0]);

		for (int i = 0; i < setArr.length; i++) {
			Set<String> subSet = new TreeSet<String>();
			for (int j = 0; j < setArr.length; j++) {
				if (i != j) {
					subSet.add((String) setArr[j]);
				}
			}
			result.add(subSet);
		}
		return result;
	}

	/**
	 * 功能：打印频繁项集
	 */
	void printSet(List<Set<String>> setList, int i) {
		System.out.print("频繁" + i + "项集： 共" + setList.size() + "项： {");
		for (Set<String> set : setList) {
			System.out.print("[");
			for (String str : set) {
				System.out.print(str + " ");
			}
			System.out.print("], ");
		}
		System.out.println("}");
	}
	
}