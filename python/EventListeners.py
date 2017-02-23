from burp import IBurpExtender
from burp import IHttpListener
from burp import IProxyListener
from burp import IScannerListener
from burp import IExtensionStateListener
from java.io import PrintWriter

class BurpExtender(IBurpExtender, IHttpListener, IProxyListener, IScannerListener, IExtensionStateListener):
    
    #
    # implement IBurpExtender
    #
    
    def	registerExtenderCallbacks(self, callbacks):
        # keep a reference to our callbacks object
        self._callbacks = callbacks
        
        # set our extension name
        callbacks.setExtensionName("Event listeners")
        
        # obtain our output stream
        self._stdout = PrintWriter(callbacks.getStdout(), True)

        # register ourselves as an HTTP listener
        callbacks.registerHttpListener(self)
        
        # register ourselves as a Proxy listener
        callbacks.registerProxyListener(self)
        
        # register ourselves as a Scanner listener
        callbacks.registerScannerListener(self)
        
        # register ourselves as an extension state listener
        callbacks.registerExtensionStateListener(self)
    
    #
    # implement IHttpListener
    #

    def processHttpMessage(self, toolFlag, messageIsRequest, messageInfo):
        self._stdout.println(
                ("HTTP request to " if messageIsRequest else "HTTP response from ") +
                messageInfo.getHttpService().toString() +
                " [" + self._callbacks.getToolName(toolFlag) + "]")

    #
    # implement IProxyListener
    #

    def processProxyMessage(self, messageIsRequest, message):
        self._stdout.println(
                ("Proxy request to " if messageIsRequest else "Proxy response from ") +
                message.getMessageInfo().getHttpService().toString())

    #
    # implement IScannerListener
    #

    def newScanIssue(self, issue):
        self._stdout.println("New scan issue: " + issue.getIssueName())

    #
    # implement IExtensionStateListener
    #

    def extensionUnloaded(self):
        self._stdout.println("Extension was unloaded")
