/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#include "FileOutputStream.h"

using namespace v8;

namespace titanium {

FileOutputStream::FileOutputStream(const char* path)
{
	fp_ = fopen(path, "w");
}

FileOutputStream::~FileOutputStream()
{
	fclose(fp_);
}

void FileOutputStream::EndOfStream()
{
	fflush(fp_);
}

OutputStream::WriteResult FileOutputStream::WriteAsciiChunk(char* data, int size)
{
	const size_t len = static_cast<size_t>(size);
	size_t off = 0;

	while (off < len) {
		if (feof(fp_) || ferror(fp_)) {
			return kAbort;
		}
		off += fwrite(data + off, 1, len - off, fp_);
	}

	return kContinue;
}

} // namespace titanium

