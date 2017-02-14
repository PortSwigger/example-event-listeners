package burp;

import java.io.PrintWriter;

public class BurpExtender implements IBurpExtender, IHttpListener, 
        IProxyListener, IScannerListener, IExtensionStateListener
{
    private IBurpExtenderCallbacks callbacks;
    private PrintWriter stdout;
    
    //
    // implement IBurpExtender
    //
    
    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // keep a reference to our callbacks object
        this.callbacks = callbacks;
        
        // set our extension name
        callbacks.setExtensionName("Event listeners");
        
        // obtain our output stream
        stdout = new PrintWriter(callbacks.getStdout(), true);
        
        // register ourselves as an HTTP listener
        callbacks.registerHttpListener(this);
        
        // register ourselves as a Proxy listener
        callbacks.registerProxyListener(this);
        
        // register ourselves as a Scanner listener
        callbacks.registerScannerListener(this);
        
        // register ourselves as an extension state listener
        callbacks.registerExtensionStateListener(this);
    }

    //
    // implement IHttpListener
    //

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo)
    {
        stdout.println(
                (messageIsRequest ? "HTTP request to " : "HTTP response from ") +
                messageInfo.getHttpService() +
                " [" + callbacks.getToolName(toolFlag) + "]");
    }

    //
    // implement IProxyListener
    //

    @Override
    public void processProxyMessage(boolean messageIsRequest, IInterceptedProxyMessage message)
    {
        stdout.println(
                (messageIsRequest ? "Proxy request to " : "Proxy response from ") +
                message.getMessageInfo().getHttpService());
    }

    //
    // implement IScannerListener
    //

    @Override
    public void newScanIssue(IScanIssue issue)
    {
        stdout.println("New scan issue: " + issue.getIssueName());
    }

    //
    // implement IExtensionStateListener
    //

    @Override
    public void extensionUnloaded()
    {
        stdout.println("Extension was unloaded");
    }
}