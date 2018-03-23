# xmpp-ionic3
This plugin is a plugin for the Openfire. When you send a message, this plugin add the open graph tag to the message.

## Requirements
* [Openfire 4.2.0](https://www.igniterealtime.org/downloads/) or higher

## Soruce code clone
git clone https://github.com/junglestory/openfire-opengraph.git

## Installation
Copy `opengraph.jar` into the plugins directory of your Openfire server, or use the Openfire Admin Console to upload the plugin. The plugin will then be automatically deployed.

To upgrade to a new version, copy the new `opengraph.jar` file over the existing file.

## Packets
When you send a url message, the Opengraph plugin adds `<x xmlns="jabber:x:og"> </x>` to the message.

```shell
<message xmlns="jabber:client" to="user01@localhost/4vv9hioquz" from="001521447586369@conference.localhost/user01" type="groupchat" id="3">
	<body>https://github.com</body>
	<x xmlns="jabber:x:event"><composing/></x>
	<x xmlns="jabber:x:og">
		<title>Build software better, together</title>
		<image>https://assets-cdn.github.com/images/modules/open_graph/github-logo.png</image>
		<url>https://github.com</url>
		<description>GitHub is where people build software. More than 27 million people use GitHub to discover, fork, and contribute to over 80 million projects.</description>
	</x>
	<data xmlns="jabber:x:data" from="user01@localhost" roomID="5" stamp="2018-03-23T02:14:20.910Z"/>
</message>
```

## License
This software is released under the MIT license.
