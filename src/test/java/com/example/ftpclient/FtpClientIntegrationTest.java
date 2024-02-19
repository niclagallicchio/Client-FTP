package com.example.ftpclient;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FtpClientIntegrationTest {

	private FtpClient ftpClient;

	// set client parameters
	@Before
	public void setup() throws IOException {

		ftpClient = new FtpClient("ftp.dlptest.com", 21, "dlpuser", "rNrKYTX9g7z3RgJRmxWuGHbeu");
		ftpClient.open();
	}

	// close the client
	@After
	public void teardown() throws IOException {
		ftpClient.close();
	}

	// verify that the file is present in the list of files contained in the FTP
	// server
	@Test
	public void givenRemoteFile_whenListingRemoteFiles_thenItIsContainedInList() throws IOException {
		Collection<String> files = ftpClient.listFiles("");

		assertThat(files).contains("test.csv");
	}

	// verify that the file is NOT present in the list of files contained in the FTP
	// server
	@Test
	public void givenRemoteFile_whenListingRemoteFiles_thenItIsNotContainedInList() throws IOException {
		Collection<String> files = ftpClient.listFiles("");

		assertThat(files).doesNotContain("foobar.txt");
	}

	// downloads the file from the ftp server, renames it and checks if it exists,
	// then deletes it
	@Test
	public void givenRemoteFile_whenDownloading_thenItIsOnTheLocalFilesystem() throws IOException {
		ftpClient.downloadFile("/test.csv", "downloaded_test.csv");

		assertThat(new File("downloaded_test.csv")).exists();
		new File("downloaded_test.csv").delete(); // cleanup
	}

	
	//upload a file to the ftp server, ask for the list of files and check if the file is uploaded
	@Test
	public void givenLocalFile_whenUploadingIt_thenItExistsOnRemoteLocation() throws URISyntaxException, IOException {
		File file = new File(getClass().getClassLoader().getResource("ftp/baz.txt").toURI());

		ftpClient.putFileToPath(file, "/buz.txt");

		Collection<String> files = ftpClient.listFiles("");

		assertThat(files).contains("buz.txt");
	}

}
