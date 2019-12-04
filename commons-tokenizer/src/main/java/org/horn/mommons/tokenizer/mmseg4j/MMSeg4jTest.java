package org.horn.mommons.tokenizer.mmseg4j;

import com.chenlb.mmseg4j.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.StringReader;

public class MMSeg4jTest {
	public static void main(String[] args) throws IOException {
		String line = "16日上午，指挥中心召开全体中层干部会议，传达学习全国公安厅局长座谈会精神，"
				+ "通报全市公安窗口单位贯彻落实省厅两个《措施》检查情况，研究部署当前重点工作。"
				+ "周兆洪主任要求中心各部门：一要紧紧围绕中秋、国庆及“9.18”敏感期，强化情报信息"
				+ "收集研判和敏感舆情监测处置，全力做好当前维稳工作；二要紧紧围绕省厅常州会议部署"
				+ "的110接处警系统升级改造、智能指挥调度平台建设等五个项目任务，会同相关部门逐一立"
				+ "项推进，加快推进指挥中心信息化建设；三要紧紧围绕省厅“二十二”条双服务措施，结合扬"
				+ "州实际，细化贯彻意见，推进指挥中心规范化建设；四要紧紧围绕提升公安指挥效能，精心"
				+ "组织开展情报信息和接处警比武竞赛，推动能力素质提升；五要紧紧围绕队伍教育整顿活动，"
				+ "强化队伍教育管理，推动公安指挥队伍健康发展。";
		Dictionary dic = Dictionary.getInstance();
		Seg seg = new SimpleSeg(dic);
		StopWordFilter stopFilter = new StopWordFilter();
		stopFilter.init();
		
		MMSeg mmSeg = new MMSeg(new StringReader(line), seg);
		Word word = null;
		String resultWord;
		StringBuilder sb = new StringBuilder();
		while ((word = mmSeg.next()) != null) {
			if (word.getLength() >= 2) {
				// 词典停用词过滤
				resultWord = stopFilter.excludeStopWord(word.toString());
				if (StringUtils.isNotBlank(resultWord)) {//判断某字符串是否不为空且长度不为0且不由空白符(whitespace)构成
					resultWord = NumberFilter.resultWord(resultWord);// 数字过滤
					if (StringUtils.isNotBlank(resultWord)) {
						sb.append(resultWord+" ");
					}
				}
			}
		}
		System.out.println(sb.toString());
	}
}
