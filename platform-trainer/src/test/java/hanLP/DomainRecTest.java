package hanLP;


import com.google.common.collect.Sets;
import fid.platform.core.common.commonutil.AnsjLibraryLoader;
import fid.platform.core.common.commonutil.DomainParseUtil;
import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.ansj.app.crf.model.CRFppTxtModel;
import org.ansj.dic.LearnTool;
import org.ansj.domain.Nature;
import org.ansj.domain.NewWord;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.AmbiguityLibrary;
import org.ansj.library.CrfLibrary;
import org.ansj.library.DicLibrary;
import org.ansj.library.NatureLibrary;
import org.ansj.recognition.impl.NatureRecognition;
import org.ansj.recognition.impl.UserDicNatureRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;
import org.nlpcn.commons.lang.util.WordAlert;

import java.util.List;
import java.util.Set;

public class DomainRecTest {

		@Test
		public void testSegment() {

				String testStr = "借着资源涨价的大势，罗平锌电（002114.SZ）今年以来涨幅可观。除了在二级市场上表现亮眼，这家公司自上市以来，也进行过多次并购重组。\n" +
								"　　然而，在这看似正常的资本运作背后，却有一家被层层迷雾包围，从未披露过的隐形关联方：罗平县宏盛金属工贸有限责任公司（下称宏盛金属）。\n" +
								"　　界面新闻记者调查发现，罗平锌电的许多运作，与宏盛金属均有着千丝万缕的联系。甚至在宏盛金属设立之时，开会场所就位于罗平锌电大楼五楼的会议室。\n" +
								"　　你的股东，我的员工\n" +
								"　　宏盛金属设立于2005年5月，初始股东（发起人）26名。\n" +
								"　　界面新闻独家获得的宏盛金属股东名册1\n" +
								"　　界面新闻独家获得的宏盛金属股东名册2\n" +
								"　　界面新闻记者查询公开资料后发现，宏盛金属的股东，有不少是在罗平锌电任职的中高管。\n" +
								"　　在宏盛金属的26名初始股东中，有一位股东名叫李江涛。他的名字不仅出现在宏盛金属的公司股东（发起人）名录中，还出现在了罗平锌电的招股书当中。招股书披露，李江涛实为是罗平锌电的监事与锌冶炼厂生产技术科科长。\n" +
								"　　事实上，宏盛金属的发起人当中，并非只有李江涛一人在罗平锌电任职。\n" +
								"　　2009年，一个名为“张芙蓉”的人出现在了罗平锌电当年的年报中。年报显示，她是罗平锌电第四届监事会中的一名监事，而这个名字同样也出现在了宏盛金属初始股东的名单上。\n" +
								"　　除此之外，在26人的初始股东中，秦怀坤任罗平锌电公司经理工作部经理；刘松林任罗平锌电锌厂厂长；桂腾云任罗平锌电选矿厂厂长。而徐斌与赵保灿的名字，共同出现在了罗平锌电所属的一项名为“一种在选矿过程中抑制钙、镁的方法”的专利的发明人中，专利号为CN105689148A。\n" +
								"　　时至今日，宏盛金属只剩下六名自然人股东，分别是：唐云龙、张紫家、桂俊生、陈波、殷坤、桂镜华。\n" +
								"　　原先宏盛金属的26名初始股东大部分已经退出，仅剩殷坤。但初始股东中有一位桂镜富，与现有股东桂镜华名字只有一字之差。\n" +
								"　　不断和上市公司做“生意”\n" +
								"　　宏盛金属不仅出现在罗平锌电的参股公司中，还在2006年2月，出资500万元入股云南罗平荣信稀贵金属有限公司（下称荣信稀贵），占荣信稀贵注册资本的33.3%。仅仅时隔一年多，2007年10月16日，罗平锌电就以1598.1万元的交易对价收购了宏盛金属所持有的33.3%荣信稀贵股权。\n" +
								"　　罗平锌电在其招股书中披露，荣信稀贵为其旗下控股子公司。据罗平锌电的公告显示，荣信稀贵成立之初，实际上由罗平锌电和云南华宇实业有限公司共同出资设立。该公司在2006年3月进行了增资扩股。注册资本由1000万元增加为1500万元，其中罗平锌电变更为625万元，占注册资的41.7%，云南瑞物经贸有限公司变更为375万元，占注册资本25%，宏盛金属为新吸收股东。\n" +
								"　　宏盛金属在一年半时间中一进一出，净赚近1000万元。\n" +
								"　　工商资料显示，宏盛金属旗下还拥有四家子公司，分别为景东彝族自治县正邦矿业有限公司（下称景东正邦）、南涧彝族自治县正邦矿业有限责任公司、昆明宝源通经贸有限公司（下称宝源通经贸）、景东彝族自治县昊鑫矿业有限责任公司（下称昊鑫矿业）。\n" +
								"　　一位接近宏盛金属的知情人士向界面新闻记者透露，在宏盛金属旗下的子公司中，“有几个矿都没有投产，只有探矿证。有一个矿的采矿证都过期了。”\n" +
								"　　界面新闻记者从上述公司的工商资料看到，景东正邦的探矿证确实已经于2017年5月25日过期。\n" +
								"　　此外，昊鑫矿业2016年报中，企业经营状态栏的状态为：歇业。\n" +
								"　　而宝源通经贸则是连续四年未公示年报。2015年6月28日，昆明市工商局因宝源通经贸“通过登记的住所或者经营场所无法联系的”原因，将其列为经营异常。\n" +
								"　　界面新闻记者曾前往其注册地昆明市官渡区关上北路109号，发现关上北路已更名为金汁路，而金汁路上并没有109号。\n" +
								"　　除了经营异常之外，宝源通经贸自2008年至今仍然欠着罗平锌电349.84万元的原料款。\n" +
								"　　2009年12月，罗平锌电开始了其上市以来的第二次收购。这次收购是由其旗下子公司荣信稀贵以780万元的交易对价，收购罗平县鑫宏工贸有限责任公司（下称鑫宏工贸）持有的罗平县天俊实业有限责任公司（下称天俊实业）60%的股权。\n" +
								"　　天俊实业成立于2007年10月20日。工商资料显示，其初始法人股东是宏盛金属，以600万元的注册资本持有天俊实业60%的股权，注册资本已经实缴。\n" +
								"　　不过，在天俊实业被收购的一年之前，2008年9月，宏盛金属退出，引入了鑫宏工贸，此项交易为原价转让。\n" +
								"　　值得注意的是，鑫宏工贸的注册资本仅为50万元，股东为曹定稳、吴映贤、申恩卫，经过界面新闻记者的比对，这些人并未与罗平锌电发生直接关联，并不是其股东或者董监高。上述知情人士称，鑫宏工贸或许是一个代持公司，背后的真正持有者另有其人。\n" +
								"　　直至2009年被收购，天俊实业成立仅有两年多的时间。据罗平锌电公告披露，天俊实业2008年至2016年的净利润分别为-63.40万元、244.49万元、604.78万元、540.24万元、266万元、120万元、-546.84万元、-296.13万元与354.5万元，天俊实业的业绩并不稳定。\n" +
								"　　从2010年到2016年，天俊实业累计实现1042.55万元收益，罗平锌电占60%则享受了该公司625.53万元的并表收益，换句话说当初780万元的投入，至今未能收回。虽然宏盛金属“并未赚到钱”，但接盘的鑫宏工贸却享受了180万元的转让溢价。\n" +
								"　　天俊实业与宏盛金属的关联不止于此。在宏盛金属退出之后的2009年12月17日，天俊实业更换了其法定代表人。该公司法定代表人由占股40%的姜燕辉变成了李来成。而在宏盛金属创立伊始，李来成是宏盛金属的第一任法定代表人与董事长，此外，天俊实业的董事陈波同时也在宏盛金属中担任董事。\n" +
								"　　界面新闻记者获得了一份2017年宏盛金属的公司章程修正材料，其中提到该修正案是天俊实业全体股东讨论后形成的决议。\n" +
								"　　此外，天俊实业与宏盛金属的注册地址、联系邮箱、联系电话均相同。宏盛金属自然人股东中出现了一位名叫陈波的股东，同时天俊实业中也有一位董事名叫陈波。\n" +
								"　　种种迹象表明，天俊实业与宏盛金属虽然未有明确的股权关系，但关联性非常紧密。\n" +
								"　　上述罗平锌电与宏盛金属之间的交易，已涉嫌隐瞒关联交易。公开资料显示，宏盛金属的主营业务为铅精矿、锌精矿、氧化锌粉、金属锌粉、铅锌金属、阴阳极板、防腐塑料产品、机械设备、一般仪表及各种零配件销售。上述业务与罗平锌电的主营业务有不少重合之处。\n" +
								"　　上海华荣律师事务所律师许峰对界面新闻记者表示，上市公司高管不得体外设立与上市公司构成同业竞争的企业。如果上市公司高管设立同业企业而未披露关联交易的话，罗平锌电与宏盛金属或涉嫌违规。\n" +
								"　　界面新闻记者就上述问题再三向罗平锌电方面求证，但截至发稿，并未获得罗平锌电的回复。\n" +
								"　　罗平锌电今年前三季实现营收11.13亿元，同比增长80.68%；净利润为4752.78万元，同比增长81.68%。不过，这家公司2017全年盈利增幅将出现下滑。罗平锌电预计今年全年盈利5000万元-6000万元，同比下降39.70%-27.64%。";
				testStr = testStr.replaceAll(" |　| |\n|\t", "");
//        testStr = testStr.replaceAll("[^\u4e00-\u9fa5·\\w]+", " ");
				char[] chars = WordAlert.alertStr(testStr);
				String replaceAll = new String(chars);
				//-----------ansj-----------
				System.out.println("---------------------ansj---------------------");
				Result parse = ToAnalysis.parse(replaceAll);
				System.out.println(parse);

				System.out.println("nlp分词");
				//调用learnTool
//        LearnTool lt = new LearnTool();
//        NewWord newWord = new NewWord("连云港国际物流合作基地",NatureLibrary.getNature("nt"));
//        lt.addTerm(newWord);
				NlpAnalysis nlpAnalysis = new NlpAnalysis();
				NlpAnalysis analysis = (NlpAnalysis) nlpAnalysis.setAmbiguityForest(AmbiguityLibrary.get());
				analysis.setIsRealName(true);
				Result parse1 = analysis.parse(replaceAll);
				UserDicNatureRecognition udn = new UserDicNatureRecognition();
				udn.recognition(parse1);

				System.out.println(parse1);
//        System.out.println("这次训练已经学到了: " + lt.count + " 个词!");
//        System.out.println(lt.getTopTree(100));

				//人名
				Set<String> anameTempSet = Sets.newHashSet();
				Set<String> anameSet = Sets.newHashSet();
				//机构名
				Set<String> ntSet = Sets.newHashSet();
				//个股机构
				Set<String> stockNameSet = Sets.newHashSet();
				//新识别
				Set<String> newSet = Sets.newHashSet();
				//地名
				Set<String> nsSet = Sets.newHashSet();
				//地名
				Set<String> nThemeSet = Sets.newHashSet();
				//所有名词
				Set<String> nSet = Sets.newHashSet();
				//动词
				Set<String> verbSet = Sets.newHashSet();

				List<Term> terms = parse1.getTerms();
				for (Term term : terms) {
						String natureStr = term.getNatureStr();
						if (natureStr.equals("n")) {
								nSet.add(term.getName());
						}
						if (natureStr.equals("nt") || natureStr.equals("nstockname")) {
								ntSet.add(term.getName());
						}
						if (natureStr.equals("nstockname")) {
								stockNameSet.add(term.getName());
						}
						if (natureStr.equals("nr") || natureStr.equals("nrf")) {
								anameTempSet.add(term.getName());
						}
						if (natureStr.equals("nw")) {
								newSet.add(term.getName());
						}
						if (natureStr.equals("ns")) {
								nsSet.add(term.getName());
						}
						if (natureStr.equals("ntheme")) {
								nThemeSet.add(term.getName());
						}
						if (natureStr.contains("v")) {
								verbSet.add(term.getName());
						}
				}
				//人名筛选
				for (String s : anameTempSet) {
						if (!ntSet.contains(s) && !nSet.contains(s)) {
								anameSet.add(s);
						}
				}

				System.out.println("人名有:");
				System.out.println(anameSet);

				System.out.println("机构名有:");
				System.out.println(ntSet);

				System.out.println("其中个股相关有:");
				System.out.println(stockNameSet);

				System.out.println("地名有:");
				System.out.println(nsSet);

				System.out.println("可能的主题有:");
				System.out.println(nThemeSet);

				System.out.println("可能的新词有:");
				System.out.println(newSet);

				System.out.println("动词词有:");
				System.out.println(verbSet);
		}

		public static void main(String[] args) {
        String testStr = "【欧洲电信标准化协会造访中兴通讯，近期双方将落实若干5G标准合作项目】 从中兴通讯获悉，近日，欧洲电信标准化协会总干事Luis-Jorge Romero及首席技术执行官Adrian Scrase到访中兴通讯，与公司标准战略团队进行深入交流。双方通过沟通，就未来国际标准研究展开更深层次的合作达成一致意见。近期，双方将具体落实若干5G标准相关合作项目。（证券时报）";
        DomainParseUtil.recongnizeDomain(testStr);
				System.out.println(AnsjLibraryLoader.class.getResource("/"));
				System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
//   testStr = testStr.replaceAll(" |　| |\n|\t","");
//        testStr = testStr.replaceAll("[^\u4e00-\u9fa5·\\w]+", " ");
//        char[] chars = WordAlert.alertStr(testStr);
//        String replaceAll = new String(chars);
//        System.out.println();
//				UserDicNatureRecognition nr = new UserDicNatureRecognition();
//				float db = CrfLibrary.get().cohesion("霍华德");
//        System.out.println(db);
		}

}
