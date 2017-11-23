package fid.platform.core.common.pojo.commons;

import fid.platform.core.common.constant.AnsjDicType;

import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("unchecked")
public class PsgAnalysisMain extends HashMap{

    public Set<String> getPersons() {
        return (Set<String>) this.get(AnsjDicType.NOUN_NR);
    }

    public void setPersons(Set<String> persons) {
        this.put(AnsjDicType.NOUN_NR,persons);
    }

    public Set<String> getCompNames() {
        return (Set<String>) this.get(AnsjDicType.NOUN_NT);
    }

    public void setCompNames(Set<String> compNames) {
        this.put(AnsjDicType.NOUN_NT,compNames);
    }

    public Set<String> getStockNames() {
        return (Set<String>) this.get(AnsjDicType.NOUN_NSTOCKNAME);
    }

    public void setStockNames(Set<String> stockNames) {
        this.put(AnsjDicType.NOUN_NSTOCKNAME,stockNames);
    }

    public Set<String> getPlaces() {
        return (Set<String>) this.get(AnsjDicType.NOUN_NS);
    }

    public void setPlaces(Set<String> places) {
        this.put(AnsjDicType.NOUN_NS,places);
    }

    public Set<String> getThemes() {
        return (Set<String>) this.get(AnsjDicType.NOUN_NTHEME);
    }

    public void setThemes(Set<String> themes) {
        this.put(AnsjDicType.NOUN_NTHEME,themes);
    }

    public Set<String> getVerbs() {
        return (Set<String>) this.get(AnsjDicType.VERB);
    }

    public void setVerbs(Set<String> verbs) {
        this.put(AnsjDicType.VERB,verbs);
    }

    public Set<String> getNewWords() {
        return (Set<String>) this.get(AnsjDicType.NOUN_NW);
    }

    public void setNewWords(Set<String> nws) {
        this.put(AnsjDicType.NOUN_NW,nws);
    }
}
