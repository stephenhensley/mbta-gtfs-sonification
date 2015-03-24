{
	"patcher" : 	{
		"fileversion" : 1,
		"rect" : [ 75.0, 57.0, 363.0, 409.0 ],
		"bglocked" : 0,
		"defrect" : [ 75.0, 57.0, 363.0, 409.0 ],
		"openrect" : [ 0.0, 0.0, 0.0, 0.0 ],
		"openinpresentation" : 0,
		"default_fontsize" : 10.0,
		"default_fontface" : 0,
		"default_fontname" : "Arial Bold",
		"gridonopen" : 0,
		"gridsize" : [ 8.0, 8.0 ],
		"gridsnaponopen" : 0,
		"toolbarvisible" : 1,
		"boxanimatetime" : 200,
		"imprint" : 0,
		"enablehscroll" : 1,
		"enablevscroll" : 1,
		"devicewidth" : 0.0,
		"boxes" : [ 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Output signal L",
					"numinlets" : 1,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 72.0, 360.0, 84.0, 18.0 ],
					"id" : "obj-27",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Output signal R",
					"numinlets" : 1,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 232.0, 360.0, 85.0, 18.0 ],
					"id" : "obj-30",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "outlet",
					"numinlets" : 1,
					"patching_rect" : [ 208.0, 360.0, 18.0, 18.0 ],
					"id" : "obj-31",
					"numoutlets" : 0,
					"comment" : ""
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "outlet",
					"numinlets" : 1,
					"patching_rect" : [ 160.0, 360.0, 18.0, 18.0 ],
					"id" : "obj-32",
					"numoutlets" : 0,
					"comment" : ""
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "< Invert",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 64.0, 200.0, 44.0, 18.0 ],
					"id" : "obj-26",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "<0.-1.0",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 72.0, 152.0, 42.0, 18.0 ],
					"id" : "obj-25",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Dry/Wet Signal Mixing",
					"numinlets" : 1,
					"fontname" : "Arial Bold Italic",
					"patching_rect" : [ 139.0, 11.0, 125.0, 19.0 ],
					"id" : "obj-39",
					"numoutlets" : 0,
					"fontsize" : 11.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Mix the original dry signal with the effected/wet signal",
					"numinlets" : 1,
					"fontname" : "Arial Italic",
					"patching_rect" : [ 24.0, 32.0, 285.0, 19.0 ],
					"id" : "obj-22",
					"numoutlets" : 0,
					"fontsize" : 11.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "pluggo.dryWet~",
					"frgb" : [ 0.301961, 0.337255, 0.403922, 1.0 ],
					"numinlets" : 1,
					"fontname" : "Arial Bold Italic",
					"patching_rect" : [ 24.0, 8.0, 116.0, 23.0 ],
					"textcolor" : [ 0.301961, 0.337255, 0.403922, 1.0 ],
					"id" : "obj-28",
					"numoutlets" : 0,
					"fontsize" : 14.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Wet \nInput R",
					"linecount" : 2,
					"numinlets" : 1,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 256.0, 64.0, 46.0, 29.0 ],
					"id" : "obj-21",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Wet \nInput L",
					"linecount" : 2,
					"numinlets" : 1,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 208.0, 64.0, 46.0, 29.0 ],
					"id" : "obj-20",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Dry \nInput R",
					"linecount" : 2,
					"numinlets" : 1,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 160.0, 64.0, 46.0, 29.0 ],
					"id" : "obj-19",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Dry/Wet",
					"numinlets" : 1,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 24.0, 72.0, 48.0, 18.0 ],
					"id" : "obj-16",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Dry \nInput L",
					"linecount" : 2,
					"numinlets" : 1,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 104.0, 64.0, 46.0, 29.0 ],
					"id" : "obj-14",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "%",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 72.0, 128.0, 19.0, 18.0 ],
					"id" : "obj-29",
					"numoutlets" : 0,
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "flonum",
					"prototypename" : "M4L.patching",
					"triscale" : 0.75,
					"numinlets" : 1,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 32.0, 128.0, 43.0, 18.0 ],
					"maximum" : 100.0,
					"id" : "obj-17",
					"numoutlets" : 2,
					"minimum" : 0.0,
					"outlettype" : [ "float", "bang" ],
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "+~",
					"numinlets" : 2,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 208.0, 320.0, 32.5, 18.0 ],
					"id" : "obj-13",
					"numoutlets" : 1,
					"outlettype" : [ "signal" ],
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "+~",
					"numinlets" : 2,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 160.0, 320.0, 32.5, 18.0 ],
					"id" : "obj-12",
					"numoutlets" : 1,
					"outlettype" : [ "signal" ],
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "inlet",
					"prototypename" : "M4L.Arial10",
					"numinlets" : 0,
					"patching_rect" : [ 256.0, 96.0, 18.0, 18.0 ],
					"id" : "obj-11",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"comment" : ""
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "inlet",
					"prototypename" : "M4L.Arial10",
					"numinlets" : 0,
					"patching_rect" : [ 208.0, 96.0, 18.0, 18.0 ],
					"id" : "obj-10",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"comment" : ""
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "inlet",
					"prototypename" : "M4L.Arial10",
					"numinlets" : 0,
					"patching_rect" : [ 160.0, 96.0, 18.0, 18.0 ],
					"id" : "obj-9",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"comment" : ""
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "inlet",
					"prototypename" : "M4L.Arial10",
					"numinlets" : 0,
					"patching_rect" : [ 112.0, 96.0, 18.0, 18.0 ],
					"id" : "obj-8",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"comment" : ""
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "*~ 1.",
					"numinlets" : 2,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 160.0, 256.0, 32.5, 18.0 ],
					"id" : "obj-6",
					"numoutlets" : 1,
					"outlettype" : [ "signal" ],
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "*~ 1.",
					"numinlets" : 2,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 112.0, 256.0, 32.5, 18.0 ],
					"id" : "obj-7",
					"numoutlets" : 1,
					"outlettype" : [ "signal" ],
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "!- 1.",
					"numinlets" : 2,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 32.0, 200.0, 32.5, 18.0 ],
					"id" : "obj-5",
					"numoutlets" : 1,
					"outlettype" : [ "float" ],
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "*~ 0.",
					"numinlets" : 2,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 256.0, 256.0, 32.5, 18.0 ],
					"id" : "obj-4",
					"numoutlets" : 1,
					"outlettype" : [ "signal" ],
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "*~ 0.",
					"numinlets" : 2,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 208.0, 256.0, 32.5, 18.0 ],
					"id" : "obj-3",
					"numoutlets" : 1,
					"outlettype" : [ "signal" ],
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "* 0.01",
					"numinlets" : 2,
					"fontname" : "Arial Bold",
					"patching_rect" : [ 32.0, 152.0, 37.0, 18.0 ],
					"id" : "obj-2",
					"numoutlets" : 1,
					"outlettype" : [ "float" ],
					"fontsize" : 10.0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "inlet",
					"prototypename" : "M4L.Arial10",
					"numinlets" : 0,
					"patching_rect" : [ 32.0, 96.0, 18.0, 18.0 ],
					"id" : "obj-1",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"comment" : ""
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"source" : [ "obj-13", 0 ],
					"destination" : [ "obj-31", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-12", 0 ],
					"destination" : [ "obj-32", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-4", 0 ],
					"destination" : [ "obj-13", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-3", 0 ],
					"destination" : [ "obj-12", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-6", 0 ],
					"destination" : [ "obj-13", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-7", 0 ],
					"destination" : [ "obj-12", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-2", 0 ],
					"destination" : [ "obj-5", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-2", 0 ],
					"destination" : [ "obj-3", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-2", 0 ],
					"destination" : [ "obj-4", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-17", 0 ],
					"destination" : [ "obj-2", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-11", 0 ],
					"destination" : [ "obj-4", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-10", 0 ],
					"destination" : [ "obj-3", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-9", 0 ],
					"destination" : [ "obj-6", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-8", 0 ],
					"destination" : [ "obj-7", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-1", 0 ],
					"destination" : [ "obj-17", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-5", 0 ],
					"destination" : [ "obj-7", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-5", 0 ],
					"destination" : [ "obj-6", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
 ]
	}

}
