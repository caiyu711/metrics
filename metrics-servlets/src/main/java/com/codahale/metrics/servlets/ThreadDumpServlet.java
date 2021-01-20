package com.codahale.metrics.servlets;

import com.codahale.metrics.jvm.ThreadDump;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;

/**
 * 一个HTTP servlet，它输出虚拟机中所有线程的{@code text/plain}转储。仅响应{@code GET}请求。
*/
public class ThreadDumpServlet extends HttpServlet {
    private static final long serialVersionUID = -2690343532336103046L;
    private static final String CONTENT_TYPE = "text/plain";

    private transient ThreadDump threadDump;

    @Override
    public void init() throws ServletException {
        this.threadDump = new ThreadDump(ManagementFactory.getThreadMXBean());
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(CONTENT_TYPE);
        resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
        final OutputStream output = resp.getOutputStream();
        try {
            threadDump.dump(output);
        } finally {
            output.close();
        }
    }
}
