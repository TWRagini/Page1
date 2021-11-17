package com.example.page1;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;

public class MyFTPClientFunctions {

	// Now, declare a public FTP client object.

	private static final String TAG = "MyFTPClientFunctions";
	public FTPClient mFTPClient = null;

	// Method to connect to FTP server:
	public boolean ftpConnect(String host, String username, String password,
			int port) {
		try {
			mFTPClient = new FTPClient();
			// connecting to the host
			mFTPClient.connect(host, port);

			// now check the reply code, if positive mean connection success
			if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
				// login using username & password
				boolean status = mFTPClient.login(username, password);

				/*
				 * Set File Transfer Mode
				 * 
				 * To avoid corruption issue you must specified a correct
				 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
				 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
				 * transferring text, image, and compressed files.
				 */
				mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
				mFTPClient.enterLocalPassiveMode();

				return status;
			}
		} catch (Exception e) {
			Log.e(TAG, "Error: could not connect to host " + host + "because"+ e.getMessage());
		}

		return false;
	}

	// Method to disconnect from FTP server:

	public boolean ftpDisconnect() {
		try {
			mFTPClient.logout();
			mFTPClient.disconnect();
			return true;
		} catch (Exception e) {
			Log.d(TAG, "Error occurred while disconnecting from ftp server.");
		}

		return false;
	}

	// Method to get current working directory:


	// Method to change working directory:


	// Method to list all files in a directory:

	public String[] ftpPrintFilesList(String dir_path) {
		String[] fileList = null;
		try {
			FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
			int length = ftpFiles.length;
			fileList = new String[length];
			for (int i = 0; i < length; i++) {
				String name = ftpFiles[i].getName();
				boolean isFile = ftpFiles[i].isFile();

				if (isFile) {
					fileList[i] = "File :: " + name;
					Log.i(TAG, "File : " + name);
				} else {
					fileList[i] = "Directory :: " + name;
					Log.i(TAG, "Directory : " + name);
				}
			}
			return fileList;
		} catch (Exception e) {
			e.printStackTrace();
			return fileList;
		}
	}


	public boolean ftpUpload(File srcFilePath, String desFileName
			) {
		boolean status = false;
		try {
			FileInputStream srcFileStream = new FileInputStream(srcFilePath);

			// change working directory to the destination directory
		//if (ftpChangeDirectory(desDirectory)) {
			status = mFTPClient.storeFile(desFileName+".jpg", srcFileStream);
			// }

			srcFileStream.close();

			return status;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "upload failed: " + e.getMessage());
		}

		return status;
	}
	///////////////////////////////////////////////////////////

	public boolean ftpConnect2(String host, String username, String password,
							  int port) {
		try {
			mFTPClient = new FTPClient();
			// connecting to the host
			mFTPClient.connect(host, port);

			// now check the reply code, if positive mean connection success
			if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
				// login using username & password
				boolean status = mFTPClient.login(username, password);

				/*
				 * Set File Transfer Mode
				 *
				 * To avoid corruption issue you must specified a correct
				 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
				 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
				 * transferring text, image, and compressed files.
				 */
				mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
				mFTPClient.enterLocalPassiveMode();

				return status;
			}
		} catch (Exception e) {
			Log.e(TAG, "Error: could not connect to host " + host + "because"+ e.getMessage());
		}

		return false;
	}

	// Method to disconnect from FTP server:

	public boolean ftpDisconnect2() {
		try {
			mFTPClient.logout();
			mFTPClient.disconnect();
			return true;
		} catch (Exception e) {
			Log.d(TAG, "Error occurred while disconnecting from ftp server.");
		}

		return false;
	}

	public boolean ftpUpload2(File srcFilePath, String desFileName
	) {
		boolean status = false;
		try {
			FileInputStream srcFileStream = new FileInputStream(srcFilePath);

			// change working directory to the destination directory
			//if (ftpChangeDirectory(desDirectory)) {
			status = mFTPClient.storeFile(desFileName+".jpg", srcFileStream);
			// }

			srcFileStream.close();

			return status;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "upload failed: " + e.getMessage());
		}

		return status;
	}
 ///////////////////////////////////////


	public boolean ftpConnect3(String host, String username, String password,
							   int port) {
		try {
			mFTPClient = new FTPClient();
			// connecting to the host
			mFTPClient.connect(host, port);

			// now check the reply code, if positive mean connection success
			if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
				// login using username & password
				boolean status = mFTPClient.login(username, password);

				/*
				 * Set File Transfer Mode
				 *
				 * To avoid corruption issue you must specified a correct
				 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
				 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
				 * transferring text, image, and compressed files.
				 */
				mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
				mFTPClient.enterLocalPassiveMode();

				return status;
			}
		} catch (Exception e) {
			Log.e(TAG, "Error: could not connect to host " + host + "because"+ e.getMessage());
		}

		return false;
	}

	// Method to disconnect from FTP server:

	public boolean ftpDisconnect3() {
		try {
			mFTPClient.logout();
			mFTPClient.disconnect();
			return true;
		} catch (Exception e) {
			Log.d(TAG, "Error occurred while disconnecting from ftp server.");
		}

		return false;
	}

	public boolean ftpUpload3(File srcFilePath, String desFileName
	) {
		boolean status = false;
		try {
			FileInputStream srcFileStream = new FileInputStream(srcFilePath);

			// change working directory to the destination directory
			//if (ftpChangeDirectory(desDirectory)) {
			status = mFTPClient.storeFile(desFileName+".jpg", srcFileStream);
			// }

			srcFileStream.close();

			return status;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "upload failed: " + e.getMessage());
		}

		return status;
	}

}