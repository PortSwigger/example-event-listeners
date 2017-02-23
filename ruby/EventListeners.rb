require 'java'
java_import 'burp.IBurpExtender'
java_import 'burp.IHttpListener'
java_import 'burp.IProxyListener'
java_import 'burp.IScannerListener'
java_import 'burp.IExtensionStateListener'

class BurpExtender
  include IBurpExtender, IHttpListener, IProxyListener, IScannerListener, IExtensionStateListener
    
  #
  # implement IBurpExtender
  #
  
  def	registerExtenderCallbacks(callbacks)
    # keep a reference to our callbacks object
    @callbacks = callbacks
    
    # set our extension name
    callbacks.setExtensionName "Event listeners"
    
    # obtain our output stream
    @stdout = java.io.PrintWriter.new callbacks.getStdout, true
    
    # register ourselves as an HTTP listener
    callbacks.registerHttpListener self
    
    # register ourselves as a Proxy listener
    callbacks.registerProxyListener self
    
    # register ourselves as a Scanner listener
    callbacks.registerScannerListener self
    
    # register ourselves as an extension state listener
    callbacks.registerExtensionStateListener self
  end
  
  #
  # implement IHttpListener
  #

  def processHttpMessage(toolFlag, messageIsRequest, messageInfo)
    @stdout.println(
            (messageIsRequest ? "HTTP request to " : "HTTP response from ") +
            messageInfo.getHttpService.toString +
            " [" + @callbacks.getToolName(toolFlag) + "]")
  end

  #
  # implement IProxyListener
  #

  def processProxyMessage(messageIsRequest, message)
    @stdout.println(
            (messageIsRequest ? "Proxy request to " : "Proxy response from ") +
            message.getMessageInfo.getHttpService.toString)
  end

  #
  # implement IScannerListener
  #

  def newScanIssue(issue)
    @stdout.println "New scan issue: #{issue.getIssueName}"
  end

  #
  # implement IExtensionStateListener
  #

  def extensionUnloaded()
    @stdout.println "Extension was unloaded"
  end
end
