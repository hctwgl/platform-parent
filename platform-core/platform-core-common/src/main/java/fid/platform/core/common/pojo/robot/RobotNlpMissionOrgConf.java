package fid.platform.core.common.pojo.robot;

public class RobotNlpMissionOrgConf {

    private Long id;
    private String startPage;
    private String listPageUrlRegex;
    private String articlePageUrlRegex;
    private String xpath;
    private Long quantity;
    private Long missionId;
    /**
     * 爬取状态 0、未开始，1、爬取中，2、爬取完成，3、停止爬取，4、终止爬取任务
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage == null ? null : startPage.trim();
    }

    public String getListPageUrlRegex() {
        return listPageUrlRegex;
    }

    public void setListPageUrlRegex(String listPageUrlRegex) {
        this.listPageUrlRegex = listPageUrlRegex == null ? null : listPageUrlRegex.trim();
    }

    public String getArticlePageUrlRegex() {
        return articlePageUrlRegex;
    }

    public void setArticlePageUrlRegex(String articlePageUrlRegex) {
        this.articlePageUrlRegex = articlePageUrlRegex == null ? null : articlePageUrlRegex.trim();
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath == null ? null : xpath.trim();
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "RobotNlpMissionOrgConf [id=" + id + ", startPage=" + startPage + ", listPageUrlRegex="
                + listPageUrlRegex + ", articlePageUrlRegex=" + articlePageUrlRegex + ", xpath=" + xpath + ", quantity="
                + quantity + ", missionId=" + missionId + "]";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
