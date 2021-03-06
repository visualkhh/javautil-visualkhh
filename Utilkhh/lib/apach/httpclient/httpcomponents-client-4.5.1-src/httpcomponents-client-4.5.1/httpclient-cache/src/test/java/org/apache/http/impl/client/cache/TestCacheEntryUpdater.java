/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.http.impl.client.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Before;
import org.junit.Test;

public class TestCacheEntryUpdater {

    private Date requestDate;
    private Date responseDate;

    private CacheEntryUpdater impl;
    private HttpCacheEntry entry;
    private Date now;
    private Date oneSecondAgo;
    private Date twoSecondsAgo;
    private Date eightSecondsAgo;
    private Date tenSecondsAgo;
    private HttpResponse response;

    @Before
    public void setUp() throws Exception {
        requestDate = new Date(System.currentTimeMillis() - 1000);
        responseDate = new Date();

        now = new Date();
        oneSecondAgo = new Date(now.getTime() - 1000L);
        twoSecondsAgo = new Date(now.getTime() - 2000L);
        eightSecondsAgo = new Date(now.getTime() - 8000L);
        tenSecondsAgo = new Date(now.getTime() - 10000L);

        response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_NOT_MODIFIED, "Not Modified");

        impl = new CacheEntryUpdater();
    }

    @Test
    public void testUpdateCacheEntryReturnsDifferentEntryInstance()
            throws IOException {
        entry = HttpTestUtils.makeCacheEntry();
        final HttpCacheEntry newEntry = impl.updateCacheEntry(null, entry,
                requestDate, responseDate, response);
        assertNotSame(newEntry, entry);
    }

    @Test
    public void testHeadersAreMergedCorrectly() throws IOException {
        final Header[] headers = {
                new BasicHeader("Date", DateUtils.formatDate(responseDate)),
                new BasicHeader("ETag", "\"etag\"")};
        entry = HttpTestUtils.makeCacheEntry(headers);
        response.setHeaders(new Header[]{});

        final HttpCacheEntry updatedEntry = impl.updateCacheEntry(null, entry,
                new Date(), new Date(), response);


        final Header[] updatedHeaders = updatedEntry.getAllHeaders();
        assertEquals(2, updatedHeaders.length);
        headersContain(updatedHeaders, "Date", DateUtils.formatDate(responseDate));
        headersContain(updatedHeaders, "ETag", "\"etag\"");
    }

    @Test
    public void testNewerHeadersReplaceExistingHeaders() throws IOException {
        final Header[] headers = {
                new BasicHeader("Date", DateUtils.formatDate(requestDate)),
                new BasicHeader("Cache-Control", "private"),
                new BasicHeader("ETag", "\"etag\""),
                new BasicHeader("Last-Modified", DateUtils.formatDate(requestDate)),
                new BasicHeader("Cache-Control", "max-age=0"),};
        entry = HttpTestUtils.makeCacheEntry(headers);

        response.setHeaders(new Header[] {
                new BasicHeader("Last-Modified", DateUtils.formatDate(responseDate)),
                new BasicHeader("Cache-Control", "public")});

        final HttpCacheEntry updatedEntry = impl.updateCacheEntry(null, entry,
                new Date(), new Date(), response);

        final Header[] updatedHeaders = updatedEntry.getAllHeaders();

        assertEquals(4, updatedHeaders.length);
        headersContain(updatedHeaders, "Date", DateUtils.formatDate(requestDate));
        headersContain(updatedHeaders, "ETag", "\"etag\"");
        headersContain(updatedHeaders, "Last-Modified", DateUtils.formatDate(responseDate));
        headersContain(updatedHeaders, "Cache-Control", "public");
    }

    @Test
    public void testNewHeadersAreAddedByMerge() throws IOException {

        final Header[] headers = {
                new BasicHeader("Date", DateUtils.formatDate(requestDate)),
                new BasicHeader("ETag", "\"etag\"")};

        entry = HttpTestUtils.makeCacheEntry(headers);
        response.setHeaders(new Header[]{
                new BasicHeader("Last-Modified", DateUtils.formatDate(responseDate)),
                new BasicHeader("Cache-Control", "public"),});

        final HttpCacheEntry updatedEntry = impl.updateCacheEntry(null, entry,
                new Date(), new Date(), response);

        final Header[] updatedHeaders = updatedEntry.getAllHeaders();
        assertEquals(4, updatedHeaders.length);

        headersContain(updatedHeaders, "Date", DateUtils.formatDate(requestDate));
        headersContain(updatedHeaders, "ETag", "\"etag\"");
        headersContain(updatedHeaders, "Last-Modified", DateUtils.formatDate(responseDate));
        headersContain(updatedHeaders, "Cache-Control", "public");
    }

    @Test
    public void oldHeadersRetainedIfResponseOlderThanEntry()
            throws Exception {
        final Header[] headers = {
                new BasicHeader("Date", DateUtils.formatDate(oneSecondAgo)),
                new BasicHeader("ETag", "\"new-etag\"")
        };
        entry = HttpTestUtils.makeCacheEntry(twoSecondsAgo, now, headers);
        response.setHeader("Date", DateUtils.formatDate(tenSecondsAgo));
        response.setHeader("ETag", "\"old-etag\"");
        final HttpCacheEntry result = impl.updateCacheEntry("A", entry, new Date(),
                new Date(), response);
        assertEquals(2, result.getAllHeaders().length);
        headersContain(result.getAllHeaders(), "Date", DateUtils.formatDate(oneSecondAgo));
        headersContain(result.getAllHeaders(), "ETag", "\"new-etag\"");
    }

    @Test
    public void testUpdatedEntryHasLatestRequestAndResponseDates()
            throws IOException {
        entry = HttpTestUtils.makeCacheEntry(tenSecondsAgo, eightSecondsAgo);
        final HttpCacheEntry updated = impl.updateCacheEntry(null, entry,
                twoSecondsAgo, oneSecondAgo, response);

        assertEquals(twoSecondsAgo, updated.getRequestDate());
        assertEquals(oneSecondAgo, updated.getResponseDate());
    }

    @Test
    public void entry1xxWarningsAreRemovedOnUpdate() throws Exception {
        final Header[] headers = {
                new BasicHeader("Warning", "110 fred \"Response is stale\""),
                new BasicHeader("ETag", "\"old\""),
                new BasicHeader("Date", DateUtils.formatDate(eightSecondsAgo))
        };
        entry = HttpTestUtils.makeCacheEntry(tenSecondsAgo, eightSecondsAgo, headers);
        response.setHeader("ETag", "\"new\"");
        response.setHeader("Date", DateUtils.formatDate(twoSecondsAgo));
        final HttpCacheEntry updated = impl.updateCacheEntry(null, entry,
                twoSecondsAgo, oneSecondAgo, response);

        assertEquals(0, updated.getHeaders("Warning").length);
    }

    @Test
    public void entryWithMalformedDateIsStillUpdated() throws Exception {
        final Header[] headers = {
                new BasicHeader("ETag", "\"old\""),
                new BasicHeader("Date", "bad-date")
        };
        entry = HttpTestUtils.makeCacheEntry(tenSecondsAgo, eightSecondsAgo, headers);
        response.setHeader("ETag", "\"new\"");
        response.setHeader("Date", DateUtils.formatDate(twoSecondsAgo));
        final HttpCacheEntry updated = impl.updateCacheEntry(null, entry,
                twoSecondsAgo, oneSecondAgo, response);

        assertEquals("\"new\"", updated.getFirstHeader("ETag").getValue());
    }

    @Test
    public void entryIsStillUpdatedByResponseWithMalformedDate() throws Exception {
        final Header[] headers = {
                new BasicHeader("ETag", "\"old\""),
                new BasicHeader("Date", DateUtils.formatDate(tenSecondsAgo))
        };
        entry = HttpTestUtils.makeCacheEntry(tenSecondsAgo, eightSecondsAgo, headers);
        response.setHeader("ETag", "\"new\"");
        response.setHeader("Date", "bad-date");
        final HttpCacheEntry updated = impl.updateCacheEntry(null, entry,
                twoSecondsAgo, oneSecondAgo, response);

        assertEquals("\"new\"", updated.getFirstHeader("ETag").getValue());
    }

    @Test
    public void cannotUpdateFromANon304OriginResponse() throws Exception {
        entry = HttpTestUtils.makeCacheEntry();
        response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        try {
            impl.updateCacheEntry("A", entry, new Date(), new Date(),
                    response);
            fail("should have thrown exception");
        } catch (final IllegalArgumentException expected) {
        }
    }

    private void headersContain(final Header[] headers, final String name, final String value) {
        for (final Header header : headers) {
            if (header.getName().equals(name)) {
                if (header.getValue().equals(value)) {
                    return;
                }
            }
        }
        fail("Header [" + name + ": " + value + "] not found in headers.");
    }

}
