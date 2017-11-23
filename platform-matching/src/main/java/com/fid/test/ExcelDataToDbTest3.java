package com.fid.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fid.ansj.AnsjLibraryLoader;
import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotMatchKwRelation;
import com.fid.domain.RobotMatchWords;
import com.fid.domain.RobotNlpTags;
import com.fid.service.RobotMatchKeywordsService;
import com.fid.service.RobotMatchKwRelationService;
import com.fid.service.RobotMatchWordsService;
import com.fid.service.RobotNlpTagsService;
import com.fid.util.ReadPropertyUtil;
import com.fid.util.StringUtil;
import com.fid.util.WordsToTagsMatch;

//@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ExcelDataToDbTest3 extends AnsjLibraryLoader {

	@Autowired
	private RobotNlpTagsService robotNlpTagsService;

	@Autowired
	private RobotMatchKeywordsService robotMatchKeywordsService;

	@Autowired
	private RobotMatchKwRelationService robotMatchKwRelationService;

	@Autowired
	private RobotMatchWordsService robotMatchWordsService;

	private Workbook workbook;
	private Sheet sheet;
	private Row row;

//	@Test
	public void printResult() throws EncryptedDocumentException, InvalidFormatException {
		// 进行运行处理处结果，等结果处理完，生成excel
		String path = ReadPropertyUtil.getPropertyByName("excel.path");
		String filePath = path + "个股公告资讯包.xls"; // allfinalModifyTest
		// allfinalModify
		InputStream is = null;
		try {
			String ext = filePath.substring(filePath.lastIndexOf("."));
			is = new FileInputStream(filePath);
			if (".xls".equals(ext)) {
				workbook = new HSSFWorkbook(is);
			} else if (".xlsx".equals(ext)) {
				workbook = new XSSFWorkbook(is);
			} else {
				workbook = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 获得工作薄的个数
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			sheet = workbook.getSheetAt(i);
			// 工作薄的名称
			String sheetName = sheet.getSheetName();
			// 所有合并单元格的个数
			int numMergedRegions = sheet.getNumMergedRegions();
			int rowNum = sheet.getLastRowNum();
			for (int z = 2; z < rowNum; z++) {
				boolean isMerge = isMergedRegion(sheet, z, 0);
				// 第一列数据是否为单行
				if (!isMerge) {
					// 得到内容
					row = sheet.getRow(z);
					if (row == null) {
						continue;
					}
					Cell cell = row.getCell(0);
					if (cell == null)
						continue;
					String cString = cell.getStringCellValue().trim();
					if (StringUtil.isBlank(cString))
						continue;
					RobotNlpTags robotNlpTags = new RobotNlpTags(); // 一级标签
					robotNlpTags.setName(cString);
					// robotNlpTags.setPid(null);
					robotNlpTags.setMissionid(1L);
					robotNlpTagsService.insert(robotNlpTags);
					Long pid = robotNlpTags.getId();
					// 一级标签对应词组
					Cell cell1 = row.getCell(1);
					String stringCellValue1 = cell1.getStringCellValue().trim();
					dealDataToDbForTags(pid, stringCellValue1);
					// 二级标签
					Cell cell2 = row.getCell(2);
					if (cell2 == null)
						continue;
					String cString2 = cell2.getStringCellValue().trim();
					if (StringUtil.isBlank(cString2))
						continue;
					String stringCellValue2 = cell2.getStringCellValue().trim();
					RobotNlpTags robotNlpTags2 = new RobotNlpTags(); // 一级标签
					robotNlpTags2.setName(cString);
					robotNlpTags2.setPid(pid);
					robotNlpTags2.setMissionid(1L);
					robotNlpTagsService.insert(robotNlpTags2);
					Long pid2 = robotNlpTags2.getId();
					dealDataToDbForTags(pid2, stringCellValue2);
				}
			}
			// 第一列数据为合并单元格
			for (int x = 0; x < numMergedRegions; x++) {
				CellRangeAddress ca = sheet.getMergedRegion(x);
				int firstColumn = ca.getFirstColumn();
				int lastColumn = ca.getLastColumn();
				int firstRow = ca.getFirstRow();
				int lastRow = ca.getLastRow();

				Row fRow = sheet.getRow(firstRow);
				Cell fCell = fRow.getCell(firstColumn);
				// 得到一级标签的值
				String cellValue = getCellValue(fCell);
				System.out.println("一级标签：" + cellValue);
				if (StringUtil.isBlank(cellValue))
					continue;
				if (firstColumn != 0 && lastColumn != 0)
					continue;
				RobotNlpTags robotNlpTags = new RobotNlpTags(); // 一级标签
				robotNlpTags.setName(cellValue);
				// robotNlpTags.setPid(null);
				robotNlpTags.setMissionid(1L);
				robotNlpTagsService.insert(robotNlpTags);
				Long pid = robotNlpTags.getId();
				// 根据起始行跟结束行得到第二列词组的值
				for (int h = 0; h < numMergedRegions; h++) {
					CellRangeAddress ca2 = sheet.getMergedRegion(h);
					int firstColumn2 = ca2.getFirstColumn();
					int lastColumn2 = ca2.getLastColumn();
					int firstRow2 = ca2.getFirstRow();
					int lastRow2 = ca2.getLastRow();
					if (firstColumn2 == 1 && lastColumn2 == 1) {
						// 判断是否在当前第一列范围内
						if (firstRow2 >= firstRow && lastRow2 <= lastRow) {
							Row fRow2 = sheet.getRow(firstRow2);
							Cell fCell2 = fRow2.getCell(firstColumn2);
							// 得到一级标签的对应的词组
							String cellValue2 = getCellValue(fCell2).trim();
							if (cellValue2 != null && !StringUtil.isBlank(cellValue2)) {
								// ---------------------------------------------------------
								// 得到当前合并单元格标签对应的词组
								dealDataToDbForTags(pid, cellValue2);
							}
						}
					}
				}
				// ----------------下面为非合并单元格标签-------------
				for (int j = firstRow; j <= lastRow; j++) {
					boolean mergedRegion = isMergedRegion(sheet, j, 1);
					if (!mergedRegion) {
						// 一级标签词组字符串
						row = sheet.getRow(j);
						// 得到内容
						{
							Cell cell = row.getCell(1);
							String stringCellValue = cell.getStringCellValue().trim();
							if (cell != null && !StringUtil.isBlank(stringCellValue)) {
								// -------------------------
								dealDataToDbForTags(pid, stringCellValue);
							}
						}
					}
				}

				// ----------------------第三列合并单元格标签----------------
				for (int h = 0; h < numMergedRegions; h++) {
					CellRangeAddress ca2 = sheet.getMergedRegion(h);
					int firstColumn2 = ca2.getFirstColumn();
					int lastColumn2 = ca2.getLastColumn();
					int firstRow2 = ca2.getFirstRow();
					int lastRow2 = ca2.getLastRow();
					if (firstColumn2 == 2 && lastColumn2 == 2) {
						// 判断是否在当前第一列范围内
						if (firstRow2 >= firstRow && lastRow2 <= lastRow) {
							Row fRow2 = sheet.getRow(firstRow2);
							Cell fCell2 = fRow2.getCell(firstColumn2);
							// 得到一级标签的对应二级标签
							String cellValue2 = getCellValue(fCell2).trim();
							if (cellValue2 != null && !StringUtil.isBlank(cellValue2)) {
								// ---------------------------------------------------------
								// 得到当前合并单元格标签
								RobotNlpTags robotNlpTags2 = new RobotNlpTags(); // 一级标签
								robotNlpTags2.setName(cellValue2);
								robotNlpTags2.setPid(pid);
								robotNlpTags2.setMissionid(1L);
								robotNlpTagsService.insert(robotNlpTags2);
								Long pid2 = robotNlpTags2.getId();
								// 得到第四列词组的值
								for (int jj = firstRow2; jj <= lastRow2; jj++) {
									row = sheet.getRow(jj);
									if (row == null) {
										continue;
									}
									Cell cell = row.getCell(3);
									if (cell == null)
										continue;
									String cString = cell.getStringCellValue().trim();
									if (StringUtil.isBlank(cString))
										continue;
									// 插入
									dealDataToDbForTags(pid2, cString);
								}
							}
						}
					}
				}

				// ----------------下面第三列为非合并单元格标签-------------
				for (int j = firstRow; j <= lastRow; j++) {
					boolean mergedRegion = isMergedRegion(sheet, j, 2);
					if (!mergedRegion) {
						// 二级标签字符串
						row = sheet.getRow(j);
						// 得到内容
						{
							Cell cell = row.getCell(2);
							if (cell == null) continue;
							String stringCellValue = cell.getStringCellValue().trim();
							if (cell != null && !StringUtil.isBlank(stringCellValue)) {
								// -------------------------
								RobotNlpTags robotNlpTags2 = new RobotNlpTags(); // 一级标签
								robotNlpTags2.setName(stringCellValue);
								robotNlpTags2.setPid(pid);
								robotNlpTags2.setMissionid(1L);
								robotNlpTagsService.insert(robotNlpTags2);
								Long pid2 = robotNlpTags2.getId();
								// 插入二级标签对应词组
								{
									row = sheet.getRow(j);
									Cell cell4 = row.getCell(3);
									if (cell4 == null)
										continue;
									String cString = cell4.getStringCellValue().trim();
									if (StringUtil.isBlank(cString))
										continue;
									// 插入
									dealDataToDbForTags(pid2, cString);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getMergedRegionValue(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getCellValue(fCell);
				}
			}
		}
		return null;
	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet
	 *            工作表
	 * @param row
	 *            行下标
	 * @param column
	 *            列下标
	 * @return
	 */
	public static boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断指定的单元格是否是合并单元格,并获取返回值
	 * 
	 * @param sheet
	 *            工作表
	 * @param row
	 *            行下标
	 * @param column
	 *            列下标
	 * @return
	 */
	public static String isMergedRegionAndRetrunValue(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					// 合并单元格
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					String cellValue = getCellValue(fCell);
					return cellValue;
				}
			}
		}
		// 不是合并单元格
		Row fRow = sheet.getRow(row);
		Cell fCell = fRow.getCell(column);
		String cellValue = getCellValue(fCell);
		return cellValue;
	}

	/**
	 * 判断合并了行
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	private boolean isMergedRow(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row == firstRow && row == lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {

		if (cell == null)
			return "";

		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

			return cell.getStringCellValue();

		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

			return String.valueOf(cell.getBooleanCellValue());

		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

			return cell.getCellFormula();

		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		}
		return "";
	}

	/**
	 * 
	 * @param pid
	 *            是否需要分词处理
	 * 
	 */
	// private void dataToDbWithCache(Long pid, String childTagsStr, boolean
	// flag, List<ContentTags> list) {
	// // 插入子标签
	// Map<String, Object> map = new HashMap<>();
	// Long childId = null;
	// // 库中是否存在
	// map.put("name", childTagsStr);
	// ChildTags queryOne = childTagsService.queryOne(map);
	// map.clear();
	// if (queryOne != null) {
	// // 添加关联关系
	// map.put("pId", pid);
	// map.put("cId", queryOne.getId());
	// ParentChildTags selectByCondition =
	// parentChildTagsManager.selectByCondition(map);
	// map.clear();
	// if (selectByCondition == null) {
	// ParentChildTags parentChildTags = new ParentChildTags();
	// parentChildTags.setcId(queryOne.getId());
	// parentChildTags.setpId(pid);
	// parentChildTagsBService.insertDBWithCache(parentChildTags);
	// childId = queryOne.getId();
	// }
	// } else {
	// // 新增后添加关联关系
	// ChildTags childTags = new ChildTags();
	// childTags.setName(childTagsStr);
	// childTagsBService.insertDBWithCache(childTags);
	// childId = childTags.getId();
	//
	// ParentChildTags parentChildTags = new ParentChildTags();
	// parentChildTags.setcId(childTags.getId());
	// parentChildTags.setpId(pid);
	// parentChildTagsBService.insertDBWithCache(parentChildTags);
	// }
	// // 这里同时插入资讯跟标签的对应关系
	// if (childId != null) {
	// if (list != null && list.size() > 0) {
	// for (ContentTags contentTags : list) {
	// contentTags.setTagid(childId);
	// contentTags.setTagname(childTagsStr);
	// contentTags.setPid(pid);
	// }
	// // 批量插入
	// contentTagsService.addBatch(list);
	// }
	// // 插入子标签的关键词
	// if (flag) {
	// dataToDbServiceTest.insertBatchToKeyWords(childId, childTagsStr);
	// } else {
	// dataToDbServiceTest.insertBatchToKeyWordsOther(childId, childTagsStr);
	// }
	// }
	// }

	// private List<ContentTags> dealStrCotent(String str) {
	// List<ContentTags> rTList = new ArrayList<>();
	// if (str != null && !"".equals(str)) {
	// String content = str.replaceAll("\\s{3,}", "\n");
	// String[] contents = content.split("\n");
	// for (String string : contents) {
	// if (StringUtils.isBlank(string)) {
	// continue;
	// }
	// ContentTags contentTags = new ContentTags();
	// contentTags.setMessage(string.trim().replaceAll("\\s+", ""));
	// contentTags.setMsgid(0L);
	// rTList.add(contentTags);
	// }
	// }
	// return rTList;
	// }

	private void dealDataToDbForTags(Long pid, String needString) {
		// 处理标签词组
		needString = needString.replace("；", " ").replace("/", ",");
		Set<String> tags = new HashSet<>();
		Set<String> tags2 = WordsToTagsMatch.getTags(tags,
				new HashSet<String>(Arrays.asList(needString.split("\\s+|,"))), needString.split("\\s+"));
		RobotMatchKeywords robotMatchKeywords = null;
		for (String string : tags2) {
			robotMatchKeywords = new RobotMatchKeywords();
			robotMatchKeywords.setName(string);
			robotMatchKeywords.settId(pid);
			robotMatchKeywords.setMissionid(1);
			robotMatchKeywordsService.insert(robotMatchKeywords);
			Long kid = robotMatchKeywords.getId();
			// 处理词组
			String[] split = string.split(",");
			RobotMatchWords robotMatchWords = null;
			RobotMatchKwRelation robotMatchKwRelation = null;
			Map<String, Object> param = new HashMap<>();
			for (String string2 : split) {
				// 个词
				robotMatchWords = new RobotMatchWords();
				robotMatchWords.setName(string2);
				robotMatchWords.setMissionid(1);
				// 插入前查询是否存在
				param.put("name", string2);
				List<RobotMatchWords> queryListByParam = robotMatchWordsService.queryListByParam(param);
				param.clear();
				if (queryListByParam != null && queryListByParam.size() > 0) {
					// 插入对应关系
					robotMatchKwRelation = new RobotMatchKwRelation();
					robotMatchKwRelation.setkId(kid);
					robotMatchKwRelation.setwId(queryListByParam.get(0).getId());
					robotMatchKwRelation.setMissionid(1);
					robotMatchKwRelationService.insert(robotMatchKwRelation);
				} else {
					// 插入个词
					robotMatchWordsService.insert(robotMatchWords);
					Long wid = robotMatchWords.getId();
					// 插入对应关系
					robotMatchKwRelation = new RobotMatchKwRelation();
					robotMatchKwRelation.setkId(kid);
					robotMatchKwRelation.setwId(wid);
					robotMatchKwRelation.setMissionid(1);
					robotMatchKwRelationService.insert(robotMatchKwRelation);
				}
			}
		}
	}
}
