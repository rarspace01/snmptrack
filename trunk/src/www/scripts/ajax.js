
	var http_request = false;
	function macheRequest(url)
	{
		http_request = false;
		if (window.XMLHttpRequest)
		{
			http_request = new XMLHttpRequest();
			if (http_request.overrideMimeType)
			{
				http_request.overrideMimeType('text/xml');
			}
		}
		else if (window.ActiveXObject)
		{
			try
			{
				http_request = new ActiveXObject("Msxml2.XMLHTTP");
			}
			catch (e)
			{
				try
				{
					http_request = new ActiveXObject("Microsoft.XMLHTTP");
				}
				catch (e)
				{
				}
			}
		}

		if (!http_request)
		{
			alert('Ende :( Kann keine XMLHTTP-Instanz erzeugen');
			return false;
		}
		http_request.open('GET', url, true);
		http_request.send(null);

	}  