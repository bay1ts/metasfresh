import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { map } from 'lodash';

import AppCard from './AppCard';

class ApplicationsScreen extends Component {
  render() {
    const { availableApplications } = this.props;

    return (
      <div className="application-container columns ml-0">
        {map(availableApplications, (app) => {
          return <AppCard key={app.id} appId={app.id} captionKey={app.caption} />;
        })}
      </div>
    );
  }
}

const mapStateToProps = ({ applications }) => {
  return {
    availableApplications: applications.availableApplications,
  };
};

ApplicationsScreen.propTypes = {
  // Props
  availableApplications: PropTypes.object.isRequired,
};

export default connect(mapStateToProps)(ApplicationsScreen);
