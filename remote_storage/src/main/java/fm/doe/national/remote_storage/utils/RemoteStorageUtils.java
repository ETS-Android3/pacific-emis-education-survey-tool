package fm.doe.national.remote_storage.utils;

import android.content.Context;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fm.doe.national.core.data.exceptions.FileExportException;
import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.utils.Constants;
import fm.doe.national.remote_storage.BuildConfig;
import fm.doe.national.remote_storage.data.model.DriveType;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import io.reactivex.Single;

public class RemoteStorageUtils {

    private static final Pattern sGoogleSpreadsheetIdPattern = Pattern.compile("/spreadsheets/d/([a-zA-Z0-9-_]+)");

    public static Single<Uri> downloadReport(Context appContext,
                                             RemoteStorage remoteStorage,
                                             FilesRepository filesRepository,
                                             String fileId) {

        return Single.fromCallable(() -> {
            File downloadFile = filesRepository.createFile(fileId, "." + BuildConfig.EXTENSION_REPORT_TEMPLATE);

            if (!downloadFile.exists()) {
                throw new FileExportException("Failed to create file");
            }

            return downloadFile;
        })
                .flatMap(downloadFile -> remoteStorage.downloadContent(fileId, downloadFile, DriveType.EXCEL)
                        .andThen(Single.just(FileProvider.getUriForFile(appContext, Constants.AUTHORITY_FILE_PROVIDER, downloadFile))));
    }

    public static Single<Uri> downloadReportFromUrl(Context appContext,
                                                    RemoteStorage remoteStorage,
                                                    FilesRepository filesRepository,
                                                    String url) {
        Matcher matcher = sGoogleSpreadsheetIdPattern.matcher(url);

        if (matcher.find()) {
            return downloadReport(appContext, remoteStorage, filesRepository, matcher.group(1));
        }

        return Single.error(new FileExportException("Failed to find fileId"));
    }

}
