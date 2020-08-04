package com.eliotlash.mclib.utils.files;

import com.eliotlash.mclib.utils.files.entries.AbstractEntry;
import com.eliotlash.mclib.utils.files.entries.FileEntry;
import com.eliotlash.mclib.utils.files.entries.FolderEntry;

import java.util.Comparator;

/**
 * Abstract entry sorter
 *
 * Sorts by folder first, and then by display name (title)
 */
public class EntrySorter implements Comparator<AbstractEntry>
{
    @Override
    public int compare(AbstractEntry o1, AbstractEntry o2)
    {
        if (o1 instanceof FolderEntry && o2 instanceof FileEntry)
        {
            return -1;
        }
        else if (o1 instanceof FileEntry && o2 instanceof FolderEntry)
        {
            return 1;
        }

        return o1.title.compareToIgnoreCase(o2.title);
    }
}
