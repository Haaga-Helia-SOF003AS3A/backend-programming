package fi.haagahelia.filedemo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;


@Entity
public class FileModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String fileName, mimeType, base64str;

	@Lob
	private byte[] file;
	
	public FileModel() {}
	
	public FileModel(String fileName, String mimeType, byte[] file) {
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.file = file;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}


}
