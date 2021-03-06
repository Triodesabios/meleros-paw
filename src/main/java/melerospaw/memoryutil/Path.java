package melerospaw.memoryutil;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;

/**
 * Object representation of a path. A {@code Path} can be created using the {@link Builder}
 * class and then obtained in its {@code String} form using {@link Path#getPath()}. We encourage
 * you to use {@code Path} when calling {@code MemoryUtils}' methods because this way methods
 * will take care of creating intermediate folders in the path built for you.
 */
public class Path {

    private static final String TAG = Path.class.getSimpleName();

    public static final int STORAGE_PRIVATE_INTERNAL = 0;
    public static final int STORAGE_PRIVATE_EXTERNAL = 1;
    public static final int STORAGE_PUBLIC_EXTERNAL = 2;
    public static final int STORAGE_PREDEFINED_PUBLIC_EXTERNAL = 3;
    public static final int STORAGE_PREDEFINED_PRIVATE_EXTERNAL = 4;

    public static final String TYPE_ALARMS = "alarms";
    public static final String TYPE_DCIM = "dcim";
    public static final String TYPE_DOCUMENTS = "documents";
    public static final String TYPE_DOWNLOADS = "downloads";
    public static final String TYPE_MOVIES = "movies";
    public static final String TYPE_MUSIC = "music";
    public static final String TYPE_NOTIFICATIONS = "notifications";
    public static final String TYPE_PICTURES = "pictures";
    public static final String TYPE_PODCASTS = "podcasts";
    public static final String TYPE_RINGTONES = "ringtones";

    private String mBasePath;
    private LinkedList<String> mFolders;
    private String mFileName;

    /**
     * Defines constants beginning with {@code "TYPE"} in this class to reference predefined folders
     * in external public or private storage directories to be used when calling
     * {@link Builder#storageDirectory(int, String)}.
     */
    @StringDef({TYPE_ALARMS, TYPE_DCIM, TYPE_DOCUMENTS, TYPE_DOWNLOADS, TYPE_MOVIES, TYPE_MUSIC,
            TYPE_NOTIFICATIONS, TYPE_PICTURES, TYPE_PODCASTS, TYPE_RINGTONES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExternalDirectoryType {
    }

    /**
     * Defines constants to refer to external and internal directories available for saving/loading
     * files in an Android device, used when calling {@link Builder#storageDirectory(int, String)}.
     * These are:
     * <ul>
     * <li>{@link #STORAGE_PRIVATE_EXTERNAL}</li>
     * <li>{@link #STORAGE_PUBLIC_EXTERNAL}</li>
     * <li>{@link #STORAGE_PRIVATE_INTERNAL}</li>
     * </ul>
     */
    @IntDef({STORAGE_PUBLIC_EXTERNAL, STORAGE_PRIVATE_EXTERNAL, STORAGE_PRIVATE_INTERNAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Directory {
    }

    /**
     * Defines constants to refer to predefined private/public external directories available for
     * saving/loading files in an Android device, to be used when calling
     * {@link Builder#storageDirectory(int, String)}. These are:
     * <ul>
     * <li>{@link #STORAGE_PREDEFINED_PUBLIC_EXTERNAL}</li>
     * <li>{@link #STORAGE_PREDEFINED_PRIVATE_EXTERNAL}.</li>
     * </ul>
     */
    @IntDef({STORAGE_PREDEFINED_PUBLIC_EXTERNAL, STORAGE_PREDEFINED_PRIVATE_EXTERNAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PredefinedDirectory {
    }

    private Path(String mBasePath) {
        this.mBasePath = mBasePath;
        mFolders = new LinkedList<>();
    }

    /**
     * Returns the list of folders that will be appended to the {@link #mBasePath} to create the
     * full path. Folders will be appended to the path in the same order they were added.
     *
     * @return A {@code LinkedList<String>} with as many elements as folders will be appended to the
     * path.
     */
    public LinkedList<String> getFolders() {
        return mFolders;
    }

    private void setFolders(LinkedList<String> mFolders) {
        this.mFolders = mFolders;
    }

    /**
     * Returns the path to the base folder referenced. This value will be the path to the
     * directory in the device specified in the method {@link Builder#storageDirectory(int)},
     * {@link Builder#storageDirectory(int, String)} or
     * {@link Builder#databaseDirectory(String)}.
     *
     * @return A {@code String} containing the path to the base directory on the device.
     */
    public String getBasePath() {
        return mBasePath;
    }

    private void setBasePath(String mBasePath) {
        this.mBasePath = mBasePath;
    }

    /**
     * Returns parameter of the destination file that will be appended at the end of the path.
     *
     * @return The parameter of destination file or an empty string if no file parameter is specified
     * during the building process.
     */
    public String getFileName() {
        return mFileName;
    }

    private void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    /**
     * Returns the full path composed by the base storage directory, the folders and the file
     * parameter specified during the building of the object.
     *
     * @return A {@code String} with the path.
     */
    public String getPath() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(mBasePath);

        for (String folder : mFolders) {
            stringBuilder.append("/").append(folder);
        }

        if (!TextUtils.isEmpty(mFileName)) {
            stringBuilder.append("/").append(mFileName);
        }

        return stringBuilder.toString();
    }

    /**
     * Returns the path from a {@code Path} object or an empty {@code String} if the Path is
     * null.
     *
     * @param path {@code Path} object to get the path from.
     * @return The path resulting from calling {@link #getPath()} or an empty String if it's
     * null.
     */
    static String getPathOrNull(Path path) {
        return path == null ? null : path.getPath();
    }

    /**
     * Returns a {@code File} pointing to the path contained in this {@code Path} object
     * specified with {@link Builder#file(String)} during the bulding process.
     *
     * @return A {@code File} object.
     */
    public File getFile() {
        return new File(getPath());
    }

    @Override
    public String toString() {
//        StringBuilder toString = new StringBuilder();
//        toString.append("BASE PATH: ");
//
//        if (TextUtils.isEmpty(mBasePath)) {
//            toString.append("No base path specified.");
//        } else {
//            toString.append(mBasePath);
//        }
//
//        if (!mFolders.isEmpty()) {
//            toString.append("\nFOLDERS: ");
//            for (String folder : mFolders) {
//                toString.append("/").append(folder);
//            }
//        }
//
//        if (!TextUtils.isEmpty(mFileName)) {
//            toString.append("\nFILE: ").append("/").append(mFileName);
//        }
//
//        return toString.toString();
        return getPath();
    }


    /**
     * <p>Class to build a {@code Path}. You'll need to call at least
     * {@link Builder#storageDirectory} or {@link Builder#databaseDirectory(String)} and,
     * optionally {@link Builder#folder(String)} and {@link Builder#file(String)}.</p>
     * <p>E.g.: to create a {@code Path} referencing
     * <i>data/data/my.application.package/files/customFolder/myFile.txt</i>, call:
     * </p>
     * <pre>{@code Path} myPath = new {@link Builder}(context)
     * .storageDirectory(Path.{@link #STORAGE_PRIVATE_INTERNAL})
     * .folder("customFolder")
     * .file("myFile.txt")
     * .build();
     * </pre>
     */
    public static class Builder {

        private Context context;

        private LinkedList<String> mFolders;
        private String mBasePath;
        private String mFileName;

        public Builder(Context context) {
            this.context = context;
            this.mFolders = new LinkedList<>();
        }

        /**
         * <p>Sets the parameter of the folders to be appended to the path after the storage directory
         * path specified with {@link #storageDirectory(int, String)},
         * {@link #storageDirectory(int)} or {@link #databaseDirectory(String)}. If you want to
         * specify a path with several folders, call this method passing the parameter of the folders
         * in the order that will appear in the path.</p>
         * E.g.: to specify path
         * <i>/myFolderA/myFolderB/myFolderC</i>, call
         * {@code builder.folder("myFolderA").folder("myFolderB").folder("myFolderC");}
         *
         * @param folderName The parameter of the folder being referenced.
         * @return The same {@code Builder} object making the call.
         */
        public Builder folder(String folderName) {
            mFolders.add(folderName);
            return this;
        }


        /**
         * Sets the parameter of the file at the very end of the path being created.
         *
         * @param fileName The parameter of the file being referenced.
         * @return The same {@code Builder} object making the call.
         */
        public Builder file(String fileName) {
            mFileName = fileName;
            return this;
        }


        /**
         * Sets the base path to one of the application's predefined external directories.
         *
         * @param directory The parameter of the external directory to be referenced.
         * @param type      Must be one of {@link Path.ExternalDirectoryType}. The type of the
         *                  predefined folder in the external directory to be referenced. For
         *                  example, {@link Path#TYPE_DCIM} will refer to folder "DCIM" in the
         *                  external directory.
         * @return The same {@link Builder} object making the call.
         */
        public Builder storageDirectory(@PredefinedDirectory int directory,
                                        @ExternalDirectoryType String type) {
            if (type == null) {
                log(context.getString(R.string.log_null_external_directory), false);
                mBasePath = StringUtil.EMPTY;

            } else {
                switch (directory) {
                    case STORAGE_PREDEFINED_PUBLIC_EXTERNAL:
                        mBasePath = usePublicExternalDirectory(type);
                        break;
                    case STORAGE_PREDEFINED_PRIVATE_EXTERNAL:
                        mBasePath = usePrivateExternalDirectory(type);
                        break;
                    default:
                        Log.e(TAG, context.getString(R.string.log_wrong_external_directory));
                }
            }

            return this;
        }

        /**
         * Sets the base path to the application's external or internal directory.
         *
         * @param directory The parameter of the base directory file to be referenced.
         * @return The same {@code Builder} object making the call.
         */
        public Builder storageDirectory(@Directory int directory) {
            switch (directory) {
                case STORAGE_PRIVATE_EXTERNAL:
                    mBasePath = usePrivateExternalDirectory();
                    break;
                case STORAGE_PUBLIC_EXTERNAL:
                    mBasePath = usePublicExternalDirectory();
                    break;
                case STORAGE_PRIVATE_INTERNAL:
                    mBasePath = usePrivateInternalDirectory();
                    break;
            }

            return this;
        }

        /**
         * Sets the base path to the application's database directory.
         *
         * @param databaseName The parameter of the database file to be referenced.
         * @return The same {@code Builder} object making the call.
         */
        public Builder databaseDirectory(String databaseName) {
            mBasePath = useDatabaseDirectory(databaseName);
            return this;
        }

        /**
         * Returns the path to the application's public external directory in an Android device.
         *
         * @return Default implementation returns
         * {@code Environment.getExternalStorageDirectory().getPath()}.
         */
        protected String usePublicExternalDirectory() {
            return Environment.getExternalStorageDirectory().getPath();
        }

        /**
         * Returns the path to the application's public external directory in an Android
         * device.
         *
         * @param type The destination directory in the public external. Use any of the
         *             following {@code Path}'s constants: {@link #TYPE_ALARMS},
         *             {@link #TYPE_DCIM}, {@link #TYPE_DOCUMENTS}, {@link #TYPE_DOWNLOADS},
         *             {@link #TYPE_MOVIES}, {@link #TYPE_MUSIC}, {@link #TYPE_NOTIFICATIONS},
         *             {@link #TYPE_PICTURES}, {@link #TYPE_PODCASTS}, {@link #TYPE_RINGTONES}.
         * @return Default implementation returns
         * {@code Environment.getExternalStoragePublicDirectory(type).getPath()}.
         */
        protected String usePublicExternalDirectory(@ExternalDirectoryType String type) {
            if (MemoryUtil.isExternalMemoryAvailable()) {
                return Environment.getExternalStoragePublicDirectory(
                        getEnvironmentDirectoryType(type)).getPath();
            } else {
                log(context.getString(R.string.log_external_storage_unavailable), false);
                return "";
            }
        }

        /**
         * Returns the path to the application's private internal directory in an Android
         * device.
         *
         * @return Default implementation returns
         * {@code context.getFilesDir().getPath()}.
         */
        protected String usePrivateInternalDirectory() {
            return context.getFilesDir().getPath();
        }

        /**
         * Returns the path to the application's private external directory in an Android
         * device.
         *
         * @return Default implementation returns
         * {@code context.getExternalFilesDir(null).getPath()}.
         */
        protected String usePrivateExternalDirectory() {
            if (MemoryUtil.isExternalMemoryAvailable()) {
                return context.getExternalFilesDir(null).getPath();
            } else {
                log(context.getString(R.string.log_external_storage_unavailable), false);
                return StringUtil.EMPTY;
            }
        }

        /**
         * Returns the path to the application's private external directory in an Android
         * device.
         *
         * @param type The destination directory in the private external memory. Use any of the
         *             following {@code Path}'s constants: {@link #TYPE_ALARMS},
         *             {@link #TYPE_DCIM}, {@link #TYPE_DOCUMENTS}, {@link #TYPE_DOWNLOADS},
         *             {@link #TYPE_MOVIES}, {@link #TYPE_MUSIC}, {@link #TYPE_NOTIFICATIONS},
         *             {@link #TYPE_PICTURES}, {@link #TYPE_PODCASTS}, {@link #TYPE_RINGTONES}.
         * @return Default implementation returns
         * {@code context.getExternalFilesDir(type).getPath()}.
         */
        protected String usePrivateExternalDirectory(@ExternalDirectoryType String type) {
            if (MemoryUtil.isExternalMemoryAvailable()) {
                return context.getExternalFilesDir(getEnvironmentDirectoryType(type)).getPath();
            } else {
                log(context.getString(R.string.log_external_storage_unavailable), false);
                return "";
            }
        }

        /**
         * Returns the path to the applications's database directory in an Android device.
         *
         * @param databaseName The parameter of the database file in the device.
         * @return Default implementation returns
         * {@code context.getDatabasePath(databaseName).getPath()}.
         */
        protected String useDatabaseDirectory(String databaseName) {
            return context.getDatabasePath(databaseName).getPath();
        }

        /**
         * Returns the {@link Environment} constant for the path to the given predefined external
         * public/private directory expressed with the {@link ExternalDirectoryType}.
         *
         * @param type The destination directory in the private external memory. Use any of the
         *             following {@code Path}'s constants: {@link #TYPE_ALARMS},
         *             {@link #TYPE_DCIM}, {@link #TYPE_DOCUMENTS}, {@link #TYPE_DOWNLOADS},
         *             {@link #TYPE_MOVIES}, {@link #TYPE_MUSIC}, {@link #TYPE_NOTIFICATIONS},
         *             {@link #TYPE_PICTURES}, {@link #TYPE_PODCASTS}, {@link #TYPE_RINGTONES}.
         * @return The {@link Environment} constant to refer the destination predefined directory.
         */

        private String getEnvironmentDirectoryType(@ExternalDirectoryType String type) {

            String folderType;

            switch (type) {
                case TYPE_ALARMS:
                    folderType = Environment.DIRECTORY_ALARMS;
                    break;
                case TYPE_DCIM:
                    folderType = Environment.DIRECTORY_DCIM;
                    break;
                case TYPE_DOCUMENTS:
                    folderType = Environment.DIRECTORY_DOCUMENTS;
                    break;
                case TYPE_MOVIES:
                    folderType = Environment.DIRECTORY_MOVIES;
                    break;
                case TYPE_MUSIC:
                    folderType = Environment.DIRECTORY_MUSIC;
                    break;
                case TYPE_NOTIFICATIONS:
                    folderType = Environment.DIRECTORY_NOTIFICATIONS;
                    break;
                case TYPE_PICTURES:
                    folderType = Environment.DIRECTORY_PICTURES;
                    break;
                case TYPE_PODCASTS:
                    folderType = Environment.DIRECTORY_PODCASTS;
                    break;
                default:
                    folderType = Environment.DIRECTORY_PICTURES;
                    break;
            }

            return folderType;
        }

        /**
         * Creates the {@code Path} object using the specified base directory, folders and file
         * parameter. You must at least call {@link #storageDirectory(int)},
         * {@link #storageDirectory(int, String)} or {@link #databaseDirectory(String)} to
         * create a valid {@code Path} object. Else a {@code RuntimeException} will be thrown.
         *
         * @return A valid Path object or throws a {@code RuntimeException} if no base directory was
         * specified.
         */
        public Path build() {

            if (!canCreatePath()) {
                throw new RuntimeException(context.getString(R.string.exception_path_incomplete));
            } else {
                Path path = new Path(mBasePath);
                path.setFolders(mFolders);
                path.setFileName(TextUtils.isEmpty(mFileName) ? StringUtil.EMPTY : mFileName);
                log(String.format(context.getString(R.string.log_path_created), path.getPath()), true);
                return path;
            }
        }

        /**
         * Tells whether the specified path can be created.
         *
         * @return Return {@code true} if a base path has been specified or else {@code false}.
         */
        public boolean canCreatePath() {
            return !TextUtils.isEmpty(mBasePath);
        }

        /**
         * Creates a duplicate of this {@code Path.Builder} object. Use this method when you want to
         * keep a {@code Path.Builder} as a reference to create different {@code Path} objects.
         * For example, if you need to reference different files in a folder, you can create
         * a {@code Path.Builder} referring to that folder and then clone it to call method
         * {@code file()} on it.
         *
         * <pre>
         * Path.Builder rootFolder = new Path.Builder(context)
         *     .storageDirectory(Path.STORAGE_PUBLIC_EXTERNAL)
         *     .folder("myFolder");
         *
         * Path myFile = rootFolder.file("myFile").build();
         * doSomethingWithMyFile(myFile);
         * MemoryUtil.clearFolder(rootFolder.build());
         *
         * // clearFolder() method will fail because you're trying to delete folder "myFolder"
         * // using "rootFolder" object, but "rootFolder" object is not referencing "myFolder"
         * // anymore since you've called file("myFile") on it in the previous lines, so now it's
         * // referencing "myFile" instead. Therefore you should have cloned "rootFolder" previous
         * // to calling file("myFile").
         *
         * Path myFile = rootFolder.getClone().file("myFile").build();
         * doSomethingWithMyFile(myFile);
         * MemoryUtil.clearFolder(rootFolder.build());
         * </pre>
         *
         * @return
         * The {@code Path.Builder} object cloned.
         */
        public Path.Builder duplicate() {

            Builder builder = new Builder(this.context);
            builder.mBasePath = this.mBasePath;
            builder.mFolders = new LinkedList<>(this.mFolders);
            builder.mFileName = this.mFileName;

            return builder;
        }

        /**
         * Creates a duplicate of this {@code Path.Builder} object. Use this method when you want to
         * keep a {@code Path.Builder} as a reference to create different {@code Path} objects.
         * For example, if you need to reference different files in a folder, you can create
         * a {@code Path.Builder} referring to that folder and then clone it to call method
         * {@code file()} on it.
         *
         * <pre>
         * Path.Builder rootFolder = new Path.Builder(context)
         *     .storageDirectory(Path.STORAGE_PUBLIC_EXTERNAL)
         *     .folder("myFolder");
         *
         * Path myFile = rootFolder.file("myFile").build();
         * doSomethingWithMyFile(myFile);
         * MemoryUtil.clearFolder(rootFolder.build());
         *
         * // clearFolder() method will fail because you're trying to delete folder "myFolder"
         * // using "rootFolder" object, but "rootFolder" object is not referencing "myFolder"
         * // anymore since you've called file("myFile") on it in the previous lines, so now it's
         * // referencing "myFile" instead. Therefore you should have cloned "rootFolder" previous
         * // to calling file("myFile").
         *
         * Path myFile = rootFolder.getClone().file("myFile").build();
         * doSomethingWithMyFile(myFile);
         * MemoryUtil.clearFolder(rootFolder.build());
         * </pre>
         *
         * @return
         * The {@code Path.Builder} object cloned.
         */
        public static Path.Builder duplicate(Path.Builder originBuilder) {

            Builder newBuilder = new Builder(originBuilder.context);
            newBuilder.mBasePath = originBuilder.mBasePath;
            newBuilder.mFolders = new LinkedList<>(originBuilder.mFolders);
            newBuilder.mFileName = originBuilder.mFileName;

            return newBuilder;
        }

        @Override
        public String toString() {
            StringBuilder toString = new StringBuilder();
            toString.append("BASE PATH: ");

            if (TextUtils.isEmpty(mBasePath)) {
                toString.append("No base path specified.");
            } else {
                toString.append(mBasePath);
            }

            if (!mFolders.isEmpty()) {
                toString.append("\nFOLDERS: ");
                for (String folder : mFolders) {
                    toString.append("/").append(folder);
                }
            }

            if (!TextUtils.isEmpty(mFileName)) {
                toString.append("\nFILE: ").append("/").append(mFileName);
            }

            return toString.toString();
        }
    }

    private static void log(String message, boolean isSuccess) {
        Logger.log(TAG, message, isSuccess);
    }
}