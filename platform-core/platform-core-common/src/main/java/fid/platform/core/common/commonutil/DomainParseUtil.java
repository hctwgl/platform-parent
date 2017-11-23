package fid.platform.core.common.commonutil;

import com.google.common.collect.Sets;
import fid.platform.core.common.pojo.commons.PsgAnalysisMain;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.UserDicNatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.util.WordAlert;

import java.util.List;
import java.util.Set;

public class DomainParseUtil extends AnsjLibraryLoader{

    public static PsgAnalysisMain recongnizeDomain(String string){
        //主体
        PsgAnalysisMain pamResult = new PsgAnalysisMain();

        String testStr = string.replaceAll(" |　| |\n|\t","");
        testStr = testStr.replaceAll("【|】"," ");
//        testStr = testStr.replaceAll("[^\u4e00-\u9fa5·\\w]+", " ");
        char[] chars = WordAlert.alertStr(testStr);
        String replaceAll = new String(chars);
        //解析开始
        Result parse1 = new NlpAnalysis().parse(replaceAll);
        UserDicNatureRecognition udn = new UserDicNatureRecognition();
        udn.recognition(parse1);
        System.out.println(parse1);

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
            String name = term.getName().toLowerCase();
            if (natureStr.equals("n")){
                nSet.add(name);
            }
            if (natureStr.equals("nt") || natureStr.equals("nstockname")){
                ntSet.add(name);
            }
            if (natureStr.equals("nstockname")){
                stockNameSet.add(name);
            }
            if (natureStr.equals("nr") || natureStr.equals("nrf")){
                anameTempSet.add(name);
            }
            if (natureStr.equals("nw")){
                newSet.add(name);
            }
            if (natureStr.equals("ns")){
                nsSet.add(name);
            }
            if (natureStr.equals("ntheme")){
                nThemeSet.add(name);
            }
            if (natureStr.contains("v")){
                verbSet.add(name);
            }
        }
        //人名筛选
        Set<String> finalRName = Sets.newHashSet();
        //机构名筛选
        Set<String> finalTName = Sets.newHashSet();

        if (anameTempSet.size()!=0){
            //人名筛选
            for (String s : anameTempSet) {
                if (!ntSet.contains(s) && !nSet.contains(s)){
                    anameSet.add(s);
                }
            }
        }

        pamResult.setPersons(anameSet);
        pamResult.setCompNames(ntSet);
        pamResult.setPlaces(nsSet);
        pamResult.setStockNames(stockNameSet);
        pamResult.setThemes(nThemeSet);
        pamResult.setVerbs(verbSet);
        pamResult.setNewWords(newSet);

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

        return pamResult;
    }

}
