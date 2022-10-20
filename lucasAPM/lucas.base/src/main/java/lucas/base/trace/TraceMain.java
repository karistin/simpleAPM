package lucas.base.trace;

import lucas.base.proxy.HttpTraceFactory;
import lucas.base.proxy.IHttpTrace;
import lucas.base.util.KeyGen;
import lucas.base.util.SysJMX;

import java.util.concurrent.ConcurrentHashMap;

/**
 * packageName    : lucas.base.trace
 * fileName       : TraceMain
 * author         : lucas
 * date           : 2022-10-17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-17        lucas       최초 생성
 */
public class TraceMain {
    public static final String[] EMPTY_PARAM = new String[0];
    private static Object lock = new Object();

    public static IHttpTrace http = null;

    public static final class stat{
        public TraceContext ctx;
        public Object req;
        public Object res;
        public  boolean isStaticContents;

        public stat(TraceContext ctx, Object req, Object res, boolean isStaticContents) {
            this.ctx = ctx;
            this.req = req;
            this.res = res;
            this.isStaticContents = isStaticContents;
        }

        public stat(TraceContext ctx) {
            this.ctx = ctx;
        }
    }

    public static Object startHttpService(Object req, Object res) {
        try {

//            TraceContext ctx = TraceContextManager.getContext(true);
//            if (ctx != null) {
//                return null;
//            }
//            if (TraceContextManager.startForceDiscard()) {
//                return null;
//            }
//            return startHttp(req, res);
        } catch (Throwable t) {
            System.out.println("Start Http Error");
            t.printStackTrace();
        }
        return null;
    }

    public static Object startHttpFilter(Object req, Object res) {
        try {
//            TraceContext ctx = TraceContextManager.getContext(true);
//            if (ctx != null) {
//                return null;
//            }
//            if (TraceContextManager.startForceDiscard()) {
//                return null;
//            }
            return startHttp(req, res);
        } catch (Throwable t) {
            System.out.println( "Start Filter Error");
            t.printStackTrace();
        }
        return null;
    }

    private static void initHttp(Object req) {
        synchronized (lock) {
            if (http == null) {
                http = HttpTraceFactory.create(req.getClass().getClassLoader(), req);
            }
        }
    }
    private static Object startHttp(Object req, Object res) {
        if (http == null) {
            initHttp(req);
        }
        return startHttp(req, res, http, false, null);
    }

    private static Object startHttp(Object req, Object res, IHttpTrace http0, boolean isReactive, Object exchange) {

        TraceContext ctx = new TraceContext();

        ctx.thread = Thread.currentThread();
        ctx.threadId = ctx.thread.getId();
        ctx.threadName = ctx.thread.getName();

        ctx.txid = KeyGen.next();
        ctx.startTime = System.currentTimeMillis();
        ctx.startCpu = SysJMX.getCurrentThreadCPU();

        ctx._req = req;
        ctx._res = res;
        ctx.http = http0;
//        ctx.serviceName = http0.getRequestURI(req);
//
//        ctx.http_method = http0.getMethod(req);
//        ctx.http_query = http0.getQueryString(req);
//        ctx.http_content_type = http0.getContentType(req);

//        System.out.println("============================");
//        System.out.println("Thread ID : " + ctx.threadId);
//        System.out.println("TxID : " + ctx.txid);
//        System.out.println("parameter : "+ http0.getParameter(req, ""));
//        System.out.println("Attribute : "+ http0.getAttribute(req, ""));
//        System.out.println("Header : " + http0.getHeader(req, "Accept"));
//        System.out.println("Connection : " + http0.getHeader(req, "Connection"));
//        System.out.println("Method : " + http0.getMethod(req));
//        System.out.println("URI : " + http0.getRequestURI(req));
//        System.out.println("ID : " + http0.getRequestId(req));
//        System.out.println("IP : " + http0.getRemoteAddr(req));
//        System.out.println("Query : " + http0.getQueryString(req));

//        System.out.println("Cookie : " + http0.getCookie(req , "JSESSIONID"));
//        ctx.bytes = SysJMX.getCurrentThreadAllocBytes(conf.profile_thread_memory_usage_enabled);
//        ctx.profile_thread_cputime = conf.profile_thread_cputime_enabled;
//        if (ctx.profile_thread_cputime) {
//        }

//
//        HashedMessageStep step = new HashedMessageStep();
//        step.time = -1;
//        step.hash = DataProxy.sendHashedMessage("[driving thread] " + ctx.threadName);
//        ctx.profile.add(step);
//
//        http0.start(ctx, req, res);

        //
//        if (ctx.isFullyDiscardService) {
//            return null;
//        }
//
//        if (ctx.serviceName == null) {
//            ctx.serviceName = "Non-URI";
//        }
//
//        TraceContextManager.start(ctx);
//
//        Stat stat = new Stat(ctx, req, res);
//        stat.isStaticContents = ctx.isStaticContents;
//
//        if (stat.isStaticContents == false) {
//            if (ctx.xType != XLogTypes.ASYNCSERVLET_DISPATCHED_SERVICE) {
//                PluginHttpServiceTrace.start(ctx, req, res, http0, isReactive);
//            }
//
//            if (plController != null) {
//                plController.start(ctx, req, res);
//            }
//        }
//        return stat;
        return null;
    }
}
