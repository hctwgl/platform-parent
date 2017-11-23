package fid.platform.core.deeplearningutil;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.io.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class TextUtils {

    private TextUtils() {

    }

    public static List<File> listFilesRecursively(String path, Predicate<File> condition) {
        File dir = new File(path);
        Preconditions.checkState(dir.exists() && dir.isDirectory(), path + " is not a directory");
        List<File> files = new ArrayList<File>();
        Files.fileTreeTraverser()
                .breadthFirstTraversal(dir)
                .filter(condition);
        return files;
    }
}
