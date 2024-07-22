package com.sauceLab.utilities;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.sauceLab.utilities.TestLogger.error;
import static com.sauceLab.utilities.TestLogger.info;

public final class FileSystemConnector {

	/**
	 * There should be no instance of this class.
	 */
	private FileSystemConnector() { }

	/**
	 * Copies a file to a system directory using an output stream.
	 * @param source - The source file to write to disk.
	 * @param dest - The destination file to save to disk.
	 */
	public static void copyFileUsingStream(
			final File source,
			final File dest) {
		try (FileInputStream is = new FileInputStream(source);
			 FileOutputStream os = new FileOutputStream(dest)) {
			final int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} catch (IOException e) {
			error("\nFailed to copy file to destination.\n"
					+ e.getMessage());
		}
	}

	/**
	 * This static helper method will look in the
	 * System.getProperty("user.dir") for the file 'path'
	 * and return the absolute path to that file path.
	 * @param path - The path to the file from the user directory including
	 *                preceding file separator.
	 * @return A {@link String} containing the absolute path of the relative
	 * path supplied.
	 */
	public static String reconcilePathToFile(final String path) {
		return System.getProperty("user.dir") + path;
	}

	/**
	 * Creates a directory at the absolute path specified and returns
	 * true if that directory exists after creation.
	 * @param path - The path to create the directory at.
	 * @return A {@link Boolean} Set to true if the folder was created.
	 */
	public static boolean mkDir(final String path) {
		File temp = new File(path);
		if (temp.mkdir()) {
			info("The directory " + path + "was made");
		}
		return temp.exists();
	}

	/**
	 * Determines whether a File exists at the given 'filePath'.
	 * @param filePath - The file path to the file.
	 * @return A {@link Boolean} set to true if file exists.
	 */
	public static boolean fileExists(final String filePath) {
		if (new File(filePath).exists()) {
			return true;
		}
		return false;
	}

	/**
	 * Delete the file at the given 'filePath'.
	 * @param filePath - The file path to the file.
	 */
	public static void deleteFile(final String filePath) {
		if (new File(filePath).delete()) {
			info("File was deleted.");
		}
	}

	/**
	 * Delete the folder at the given 'folderPath'.
	 * @param folderPath - The folderPath to the folder.
	 */
	public static void deleteDir(final String folderPath) {
		try {
			FileUtils.deleteDirectory(new File(folderPath));
		} catch (IOException e) {
			error("Error is " + e.getMessage(), e);
		}
	}

	/**
	 * Copies a directory and everything in it to the destination directory.
	 * @param directorySrc The directory to copy.
	 * @param directoryDest The directory to copy to.
	 */
	public static void copyFolder(
			final String directorySrc,
			final String directoryDest) {
		try {
			FileUtils.copyDirectory(new File(directorySrc),
					new File(directoryDest));
		} catch (IOException e) {
			error("Failed copying the source ["
					+ directorySrc
					+ "] to the destination ["
					+ directoryDest
					+ "]", e);
		}
	}

	public static void zipDirectory(Path sourceDirPath, Path zipFilePath) throws IOException {
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
			Files.walk(sourceDirPath)
					.filter(path -> !Files.isDirectory(path))
					.forEach(path -> {
						ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
						try (FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
							zipOutputStream.putNextEntry(zipEntry);
							byte[] buffer = new byte[1024];
							int length;
							while ((length = fileInputStream.read(buffer)) >= 0) {
								zipOutputStream.write(buffer, 0, length);
							}
							zipOutputStream.closeEntry();
						} catch (IOException e) {
							System.err.println("Error processing file: " + path);
							e.printStackTrace();
						}
					});
		}
	}
}
