module.exports = {
    css: {
      loaderOptions: {
        less: {
          modifyVars: {
            'primary-color': '#1DA57A',
            'link-color': '#1DA57A',
            'heading-color': '#FFFFFF',
            'border-radius-base': '2px',
          },
          javascriptEnabled: true,
        },
      },
    },
    devServer: {
      proxy: {
        '/api': {
          target: 'http://localhost:4172/',
          changeOrigin: true,
          ws: true,
        },
      },
    },
  };