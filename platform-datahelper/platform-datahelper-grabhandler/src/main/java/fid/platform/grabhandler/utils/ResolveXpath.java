package fid.platform.grabhandler.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * 解析出xpath的工具类
 * @author yangfeng
 *
 */
public class ResolveXpath {

	/**
	 * 通过给定文本分析出xpath
	 * @param text
	 * @throws IOException
	 */
	public static String getXpathByText(String url,String text) throws IOException{
		/**
		 * 1、匹配包含给定文本的div
		 * 2、遍历匹配的div，找到其直接文本子节点集合，并过滤空文本子节点,并对比查询文本，
		 */
		if(url==null || text == null || url.trim().length()==0 || text.trim().length()==0) return "参数错误！";
		/*File input = new File("F:\\资料\\笔记\\resolvexpath.html");
		Document doc = Jsoup.parse(input, "UTF-8");*/
		Document doc = Jsoup.connect(url)
				//不设置userAgent的话，有些网站会跳转到app页面
				.userAgent("User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
				.get();
//		System.out.println(doc.html());
		//    <p(\s+(class|name|id|style)\s*=\s*['"]{1}[A-Za-z0-9_~\-:;#\s]+['"]{1})*\s*>   <\/p>
		//     有些网站可能还有其他特殊符号，以后发现再修改以上正则表达式
		//     清除原来的html，替换为过滤后的html
		doc.body().html(
				doc.body().html()
				.replaceAll("<p(\\s+(class|name|id|style)\\s*=\\s*['\"]{1}[A-Za-z0-9_~\\-:;#\\s]+['\"]{1})*\\s*>", "")
				.replaceAll("</p>", "")
				.replaceAll("<ul(\\s+(class|name|id|style)\\s*=\\s*['\"]{1}[A-Za-z0-9_~\\-:;#\\s]+['\"]{1})*\\s*>", "")
				.replaceAll("</ul>", "")
				.replaceAll("<li(\\s+(class|name|id|style)\\s*=\\s*['\"]{1}[A-Za-z0-9_~\\-:;#\\s]+['\"]{1})*\\s*>", "")
				.replaceAll("</li>", "")
				.replaceAll("<font(\\s+(class|name|id|style|color)\\s*=\\s*['\"]{1}[A-Za-z0-9_~\\-:;#\\s]+['\"]{1})*\\s*>", "")
				.replaceAll("</font>", "")
				);
		//System.out.println(doc.html());
		
		Elements divs = doc.body().select(":contains("+text+")");//这里匹配到所有包含该文本div，即，包含该文本的直接父节点和间接父节点
//		System.out.println("共有"+divs.size()+"个div匹配到文本“"+text+"”");
		
		List<TextNode> texts = null;
		List<TextNode> textsfilter = null;
		Map<String, String> classNameMap = new HashMap<String, String>();
		for(Element div : divs){//这里遍历匹配到的div，然后过滤每个div的直接文本子节点。
			texts = div.textNodes();
			textsfilter = new ArrayList<TextNode>();
			for(int i=0; i<texts.size(); i++){
				if(!StringUtil.isBlank(texts.get(i).text())){
					if(texts.get(i).text().indexOf(text)!=-1){  
						textsfilter.add(texts.get(i)); 
						System.out.println(div.tagName()+"(class="+div.className()+")的第"+i+"个文本节点  ["+texts.get(i).text()+"] 与查询文本匹配！");
					}else{ 
						System.out.println(div.tagName()+"(class="+div.className()+")的第"+i+"个文本节点  ["+texts.get(i).text()+"] 与查询文本不匹配！"); 
					} 
					
				}else{
					System.out.println(div.tagName()+"(class="+div.className()+")的第"+i+"个文本节点  ["+texts.get(i).text()+"] 为空字符串！");
				}
			}
			if(textsfilter.size() != 0){
				String r = getElementAttr(div);
				if(r!=null){
					classNameMap.put(r, r);
				}
			}
		}
		// 拼接xpath格式  //div[@class=\"s_left\"]//div[@class=\"p_article\"]/outerHtml()/tidyText()
		String xpath = "";
		if(classNameMap.size() == 1){
			for(String key : classNameMap.keySet()){
				xpath = classNameMap.get(key);
			}
		}else if(classNameMap.size() > 1){
			for(String key : classNameMap.keySet()){
				xpath += classNameMap.get(key)+",";
			}
			//return "有多个元素匹配，请重新选择文本！";
			return xpath;
		}else{
			return "没有匹配到！";
		}
		return xpath;
	}
	private static String getElementAttr(Element e){
		
		String className = e.className();
		String id = e.id();
		
		if(StringUtil.isBlank(className) && StringUtil.isBlank(id)){//拿到的div没有class并且没有id
			if(e.parent() != null){
				return getElementAttr(e.parent());
			}else{
				return null;
			}
		}else{
			if(!StringUtil.isBlank(className)){
				//  //div[@class=\""+key+"\"]/outerHtml()/tidyText()
				return  "//"+e.tagName()+"[@class=\""+className+"\"]/outerHtml()/tidyText()";
			}else{
				//  //div[@id=\""+key+"\"]/outerHtml()/tidyText()
				return "//"+e.tagName()+"[@id=\""+id+"\"]/outerHtml()/tidyText()";
			}
		}
	}
	public static void test(Element e){
		System.out.println(e.tagName() + "," +e.className() + "," +e.id());
		if(e.parent() != null){
			test(e.parent());
			
		}else{
			System.out.println("parent is null");
		}
	}
	public static void main(String[] args){
		try {
//			F:\资料\笔记\resolvexpath.html
			String rst = getXpathByText("http://vip.stock.finance.sina.com.cn/q/go.php/vReport_Show/kind/lastest/rptid/3988640/index.phtml",
					"今日煤炭板块表现较差,昨日");
			System.out.println("xpath:"+rst);
//			testUrl("http://licaishi.sina.com.cn/web/index?ind_id=1&fee=all&page=1");	
			/*File input = new File("F:\\资料\\笔记\\resolvexpath.html");
			Document doc = Jsoup.parse(input, "UTF-8");
			
			Elements es = doc.select("div#testid");
			for(int i=0;i<es.size();i++){
				
				System.out.println("      （"+i+"）");
				test(es.get(i));
				
			}*/
			String classStr = rst.substring(rst.indexOf("\"")+1, rst.lastIndexOf("\""));
			String tagStr = rst.substring(rst.indexOf("//")+2, rst.lastIndexOf("["));
			String attrKey = rst.substring(rst.indexOf("@")+1, rst.lastIndexOf("="));
			System.out.println("tag:"+tagStr+"      attrKey:"+attrKey+"         attrVal:"+classStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*public static void testUrl(String url) throws IOException{
		System.out.println(url);
		Document doc = Jsoup.connect(url).userAgent("User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36").get();
		System.out.println(doc.baseUri());
		System.out.println("_______________________________________");
		Elements links = doc.getElementsByTag("a");
		
		for(Element link : links){
			String relHref = link.attr("href"); // == "/"
			String absHref = link.attr("abs:href"); // "http://www.open-open.com/"
			System.out.println(relHref);
			System.out.println(absHref);
			System.out.println("_______________________________________");
		}
		
	}*/
	
	
	
}
