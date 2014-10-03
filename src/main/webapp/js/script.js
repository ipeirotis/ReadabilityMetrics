var ReadabilityMetricsModule = (function() {
    return {
        init: function() {
            var self = this;

            $('#calculate-button').on('click', function() {
                self.getReadabilityMetrics($("#text").val());
            });

            self.getReadabilityMetrics($("#text").val());
        },

        getReadabilityMetrics: function(text) {
            var self = this;
            $('#loading').show();

            $.ajax({
                type: 'POST',
                url: self.getApiUrl() + '?text=' + encodeURIComponent(text),
                contentType: "application/json",
                dataType: 'json'
            }).done(function(metrics) {
                for(var id in metrics){
                    if(metrics.hasOwnProperty(id)){
                        $("#metric-" + id).text(metrics[id]);
                    }
                }
                $("td span").fadeIn();
                $('#loading').hide();
            }).fail(function(jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
                $('#loading').hide();
            });
        },

        getApiUrl: function() {
            var path = '/_ah/api/readability/v1/getReadabilityMetrics';
            if (/^localhost/.test(window.location.host)) {
                return 'http://' + window.location.host + path;
            } else {
                return 'https://' + window.location.host + path;
            }
        }
        
    };
}());