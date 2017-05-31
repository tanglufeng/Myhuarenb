package com.abcs.huaqiaobang.ytbt.capturePhoto;

import java.io.File;

abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}
