# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class OgItem(scrapy.Item):
    id = scrapy.Field()
    blog_heading = scrapy.Field()
    date_of_publish = scrapy.Field()
    author = scrapy.Field()
    blog_main_image = scrapy.Field()
    blog_content = scrapy.Field()
    blog_categories = scrapy.Field()