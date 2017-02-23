# Sample Burp Suite extension: event listeners

This extension demonstrates how to register listeners for various runtime
events:
- HTTP requests and responses for all Burp tools.
- HTTP messages intercepted by the Proxy.
- Addition of new scan issues.
- The extension being unloaded by the user.

The sample extension simply prints a message to its output stream when an event
occurs.

Registering an extension state listener is particularly important for any
extension that starts background threads or opens system resources (such as
files or database connections). The extension should listen for itself being
unloaded by the user, and should terminate any background threads or close any
open resources when this event occurs. This good practice enables the user to
fully unload the extension via the Burp UI.

This repository includes source code for Java, Python and Ruby.
