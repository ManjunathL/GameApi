package com.mygubbi.common;

import io.vertx.core.buffer.Buffer;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSanitizer
{
    private final static Logger LOG = LogManager.getLogger(FileSanitizer.class);

    private String filename;

    public static void main(String[] args)
    {
        if (args.length == 1)
        {
            new FileSanitizer(args[0]).sanitize();
        }
    }

    public FileSanitizer(String filename)
    {
        this.filename = filename;
    }

    public void sanitize()
    {
        this.readFile();
    }

    private void writeFile(String text)
    {
        VertxInstance.get().fileSystem().writeFile(this.filename, Buffer.buffer(text), fileResult ->
        {
            if (fileResult.succeeded())
            {
                LOG.info("File created.");
            }
            else
            {
                LOG.error("File not created.", fileResult.cause());
            }
            System.exit(1);
        });
    }

    private void readFile()
    {
        if (StringUtils.isEmpty(this.filename))
        {
            LOG.error("Not able to read file: " + this.filename);
            System.exit(1);
        }

        VertxInstance.get().fileSystem().readFile(filename, fileResult ->
        {
            if (fileResult.failed())
            {
                LOG.error("Not able to read file: " + filename, fileResult.cause());
                System.exit(1);
            }
            String text = this.sanitizeBuffer(fileResult.result());
            if (text == null)
            {
                LOG.info("Could not read data from file, bringing down server.");
                System.exit(-1);
            }
            this.writeFile(text);
        });

    }

    private String sanitizeBuffer(Buffer buffer)
    {
        String defaultEncoding = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(buffer.getBytes());
        try {
            BOMInputStream bOMInputStream = new BOMInputStream(inputStream);
            ByteOrderMark bom = bOMInputStream.getBOM();
            String charsetName = bom == null ? defaultEncoding : bom.getCharsetName();
            return IOUtils.toString(new BufferedInputStream(bOMInputStream), charsetName);
        }
        catch (Exception ex)
        {
            LOG.error("Error in the input stream. " + buffer.toString(), ex);
            return null;
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                //Ignore
            }
        }
    }

}
