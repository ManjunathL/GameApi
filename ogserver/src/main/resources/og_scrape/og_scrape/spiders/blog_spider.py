import scrapy

from og_scrape.items import OgItem


class OgSpider(scrapy.Spider):
    name = "og_scrape"
    allowed_domains = ["mygubbi.com"]
    start_urls = [
        "http://www.mygubbi.com/blogs/blog",
    ]

    def parse(self, response):
        for href in response.css("div.article-header > h5 > a::attr('href')"):
            url = response.urljoin(href.extract())
            yield scrapy.Request(url, callback=self.parse_dir_contents)


    def parse_dir_contents(self, response):
        for sel in response.xpath('//div[@class="grid"]/article'):
            item = OgItem()
            id = str(response)
            item['id'] = id[39:47]

            heading = str(sel.xpath('//h1/text()').extract())
            item['blog_heading'] = heading[3:-2]

            date = str(sel.xpath('//time/text()').extract())
            item['date_of_publish'] = date[3:-2]

            item['author'] = 'mygubbi'

            img = str(sel.xpath('//div[@class="articleImg"]/img/@src').extract())
            if (img[3:8] == '//cdn'):
                img = img[:3] + 'https:' + img[3:]
            item['blog_main_image'] = img[3:-2]

            content = str(sel.xpath('//div[@class="articleContent"]').extract())
            content = content.replace('\\n', '')
            content = content.replace('<span style=\"font-weight: 400;\">', '')
            content = content.replace('</span>', '')
            content = content.replace('style=\"font-weight: 400;\"', '')
            content = content.replace('style=\"display: block; margin-left: auto; margin-right: auto;\"', '')
            item['blog_content'] = content[37:-12]

            item['blog_categories'] = sel.xpath('//div[@class="article-tags"]/a/text()').extract()

            yield item
