/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef V8_FILE_OUTPUT_STREAM_H
#define V8_FILE_OUTPUT_STREAM_H

#include <stdio.h>

#include <v8.h>

namespace titanium {

class FileOutputStream : public v8::OutputStream {
public:
	explicit FileOutputStream(const char* path);
	virtual ~FileOutputStream();

	virtual void EndOfStream();
	virtual WriteResult WriteAsciiChunk(char* data, int size);

private:
	FILE* fp_;
};

} // namespace titanium

#endif
